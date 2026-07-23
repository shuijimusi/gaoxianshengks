package com.biyesheji.gybbysj.service.impl;

import com.biyesheji.gybbysj.service.calculateScoreService;
import org.springframework.stereotype.Service;

@Service
public class calculateScoreServiceImpl implements calculateScoreService {
    public double updateScore(double score, int time) {
        double result = score / time;
        return result;
    }
}
