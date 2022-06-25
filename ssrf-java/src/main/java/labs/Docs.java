package labs;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Docs {

   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)
   private Integer id;
   private String name;
   private String url;

   public Docs() {

   }

   public Docs(Integer id, String name, String url) {
      super();
      this.id = id;
      this.name = name;
      this.url = url;      
   }

   public Integer getId() {
      return id;
   }
   public void setId(Integer id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }

   public String getUrl() {
      return url;
   }
   public void setUrl(String url) {
      this.url = url;
   }
}
