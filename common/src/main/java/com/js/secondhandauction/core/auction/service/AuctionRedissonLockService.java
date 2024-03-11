package com.js.secondhandauction.core.auction.service;


import com.js.secondhandauction.common.exception.ErrorCode;
import com.js.secondhandauction.core.auction.domain.Auction;
import com.js.secondhandauction.core.auction.dto.AuctionResponse;
import com.js.secondhandauction.core.auction.exception.AuctionException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AuctionRedissonLockService {
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private AuctionService auctionService;

    public AuctionResponse createAuctionWithLock(Auction auction, boolean isImmediatePurchase) {
        RLock lock = redissonClient.getLock(String.format("auction:%d", auction.getItemNo()));

        AuctionResponse auctionResponse = null;
        try {
            boolean isLocked = lock.tryLock(1, 3, TimeUnit.SECONDS);
            if (!isLocked) {
                log.info("Failed to acquire a lock");
                throw new AuctionException(ErrorCode.CANNOT_ACCESS_LOCK);
            }
            auctionResponse = auctionService.createAuction(auction, isImmediatePurchase);
        } catch (InterruptedException e) {
            log.error("Lock Error" + e);
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        return auctionResponse;
    }
}
