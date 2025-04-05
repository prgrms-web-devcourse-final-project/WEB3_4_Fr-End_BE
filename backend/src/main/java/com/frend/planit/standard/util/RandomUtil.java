package com.frend.planit.standard.util;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RandomUtil {

    public String generateUid() {
        return NanoIdUtils.randomNanoId();
    }
}