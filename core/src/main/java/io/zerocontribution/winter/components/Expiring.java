package io.zerocontribution.winter.components;

import com.artemis.Component;

public class Expiring extends BaseComponent {

    public float expiration = 0;

    public Expiring(float expiration) {
        this.expiration = expiration;
    }

}
