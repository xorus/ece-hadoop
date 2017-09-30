import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Mapper {
    public Map map(List<String> data) {
        Map<Integer, String> newMap = new HashMap<Integer, String>();

        for (String line : data) {
            String[] values = line.split(",");
            for (int i = 0; i < values.length; i++) {
                if (newMap.containsKey(i)) {
                    newMap.put(i, values[i] + "," + newMap.get(i));
                } else {
                    newMap.put(i, values[i]);
                }
            }
        }

        return newMap;
    }
}

class Reducer {
    public void reduce(Map<Integer, String> sortedMap) {
        for (Map.Entry<Integer, String> entry: sortedMap.entrySet()) {
            System.out.println("context.write " + entry.getKey() + " = " + entry.getValue());
        }
    }
}


class Main {
    public static void main(String[] args) {
        Mapper mapper = new Mapper();
        Reducer reducer = new Reducer();

        List<String> data = new ArrayList<>();
        data.add("a,b,c,d");
        data.add("1,2,3,4");

        Map map = mapper.map(data);
        reducer.reduce(map);
    }
}
