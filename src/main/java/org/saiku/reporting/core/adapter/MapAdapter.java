package org.saiku.reporting.core.adapter;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapAdapter extends XmlAdapter<MapElements[], Map<String, String>> {
    public MapAdapter() {
    }

    public MapElements[] marshal(Map<String, String> arg0) throws Exception {
        MapElements[] mapElements = new MapElements[arg0.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : arg0.entrySet())
            mapElements[i++] = new MapElements(entry.getKey(), entry.getValue());

        return mapElements;
    }

    public Map<String, String> unmarshal(MapElements[] arg0) throws Exception {
        Map<String, String> r = new HashMap<String, String>();
        for (MapElements mapelement : arg0)
            r.put(mapelement.property, mapelement.value);
        return r;
    }
}