package io.zerocontribution.winter.components;

import com.artemis.Component;

public class Drop extends Component {
    public int credits = 0;
    public int itemId = 0;

    public Drop(int credits, int itemId) {
        this.credits = credits;
        this.itemId = itemId;
    }

}
