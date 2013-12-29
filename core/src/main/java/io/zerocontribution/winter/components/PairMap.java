package io.zerocontribution.winter.components;

import com.artemis.Component;
import com.badlogic.gdx.utils.ObjectMap;
import io.zerocontribution.winter.Pair;

public class PairMap extends Component {
    public ObjectMap<Pair, Boolean> map = new ObjectMap<Pair, Boolean>();
}
