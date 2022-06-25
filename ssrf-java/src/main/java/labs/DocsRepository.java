package labs;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocsRepository extends JpaRepository<Docs,Integer> {	
	public Docs findByUrl(String url);
}