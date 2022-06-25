package labs;
 
import java.util.List;
 
import javax.transaction.Transactional;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
@Service
@Transactional
public class DocsService {
 
    @Autowired
    private DocsRepository repo;
     
    public List<Docs> listAll() {
        return repo.findAll();
    }
     
    public void save(Docs product) {
        repo.save(product);
    }
     
    public Docs get(Integer id) {
        return repo.findById(id).get();
    }

    public Docs findByUrl(String url) {
        return repo.findByUrl(url);
    }
     
    public void delete(Integer id) {
        repo.deleteById(id);
    }
}