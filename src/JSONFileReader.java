import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONFileReader {
	//pairnei mia lista apo travellers k th grafei se ena json arxeio

  public void writeJSON(List<Traveller> travellers)
      throws JsonGenerationException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();
    AllTravellers data = new AllTravellers();
    data.setCollectionAllTravellers(travellers);
    mapper.writeValue(new File("travellers_arraylist.json"), data);
  }

  	//diavazei to json k to metatrepei se lista apo travellers
  public List<Traveller> readJSON()
      throws JsonParseException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enableDefaultTyping();
    AllTravellers data = mapper.readValue(new File("travellers_arraylist.json"), AllTravellers.class);
    return data.getCollectionAllTravellers();
  }
}
///ola afta ginontai mesw ths alltravellers