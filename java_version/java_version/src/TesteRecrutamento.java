import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TesteRecrutamento {

    public static void main(String[] args) throws Exception {
        Conection conection = new Conection();
        JSONArray json_items = new JSONArray(conection.get_items());
        ArrayList<Item> dezember_items = items_by_date(convert_json_to_array(json_items),
                "01/12/2018",
                "31/12/2018" );

        String item_mais_vendido = "item " + get_best_seller(dezember_items);
        double valor_total = get_total_value(get_items_by_type(dezember_items,item_mais_vendido));
        BigDecimal bd = new BigDecimal(valor_total).setScale(2, RoundingMode.HALF_EVEN);
        valor_total =  bd.doubleValue();
        
        String answer = item_mais_vendido + "#" + valor_total;
        conection.send_post(answer);
    }

    static int get_best_seller(ArrayList<Item> items){
        int index = 0;
        int [] quantidades = new int[5];
        String [] tipos_de_items = {"item 1", "item 2", "item 3", "item 4", "item 5"};
        for(int i = 0; i < tipos_de_items.length; i++){
            quantidades[i] = get_total_qtd(get_items_by_type(items, tipos_de_items[i]));
        }
        int max = 0;
        for (int i = 0; i < quantidades.length; i++) {
            if (quantidades[i] > max) {
                max = quantidades[i];
            }
        }
        for (int i = 0; i < quantidades.length ; i++) {
            if(quantidades[i] == max){
                index = i;
            }
        }
        return (index+1);
    }

    static int get_total_qtd(ArrayList<Item> list){
        int total = 0;
        for (int i = 0; i < list.size(); i++) {
            Item item = list.get(i);
            total += item.getQuantidade();
        }
        return total;
    }

    static double get_total_value(ArrayList<Item> list){
        double total = 0;
        for (int i = 0; i < list.size(); i++) {
            Item item = list.get(i);
            total += item.getTotal();
        }
        return total;
    }

    static ArrayList<Item> get_items_by_type(ArrayList<Item> items, String type_of_item){
        ArrayList<Item> items_by_type = new ArrayList<Item>();
        for(int i = 0; i < items.size(); i++ ){
            Item item = items.get(i);
            if(item.getItem().equals(type_of_item)){
                items_by_type.add(item);
            }
        }
        return items_by_type;
    }

    static ArrayList<Item> convert_json_to_array(JSONArray array_of_items) throws ParseException {
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<Item> dezember_items = new ArrayList<>();
        for (int i = 0; i < array_of_items.length(); i++) {
            JSONObject json_item = array_of_items.getJSONObject(i);
            Item item = new Item(json_item.getString("item"),
                    json_item.getDouble("total"),
                    json_item.getInt("quantidade"),
                    sourceFormat.parse(json_item.getString("dia")));
            dezember_items.add(item);
        }
        return dezember_items;
    }

    static ArrayList<Item> items_by_date(ArrayList<Item> array_of_items, String start_date, String end_date) throws ParseException {
        DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date initial_date = sourceFormat.parse(start_date);
        Date final_date = sourceFormat.parse(end_date);
        ArrayList<Item> dezember_items = new ArrayList<Item>();
        for (int i = 0; i < array_of_items.size(); i++) {
            Item item = array_of_items.get(i);
            Date date = item.getDia();
            if(date.compareTo(initial_date) > 0 && date.compareTo(final_date) < 0){
                dezember_items.add(item);
            }
            if(date.compareTo(initial_date) == 0 || date.compareTo(final_date) == 0){
                dezember_items.add(item);
            }
        }
        return dezember_items;
    }

}
