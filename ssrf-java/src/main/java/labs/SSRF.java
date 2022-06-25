package labs;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.HashMap;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;

import org.springframework.core.env.Environment;

import java.io.*;
import java.io.IOException;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URL;
import java.net.MalformedURLException;

@Controller
@RequestMapping(path="/api") 
public class SSRF {

    @Autowired    
    private DocsService docsService;     

    @Autowired
    private Environment env; 

    @GetMapping(path="/docs/render") 
    public @ResponseBody String render (@RequestParam Map<String,String> data) {                
        try { 
            String url = data.get("url");            
            System.out.println(url);
            Docs docs = docsService.findByUrl(url);
            if (docs != null){                
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet request = new HttpGet(docs.getUrl());
                CloseableHttpResponse response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                return result;                        
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found!");                  
        }
        catch (Exception e) {   
            e.printStackTrace();         
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error!");                  
        }
    }

    @PostMapping(path="/docs/create") 
    public @ResponseBody Docs addNewDocs (@RequestBody Docs docs) {                
        try {            
            Docs newDocs = new Docs();
            newDocs.setName(docs.getName());
            newDocs.setUrl(docs.getUrl());                         
            docsService.save(newDocs);             
            return newDocs;
        }
        catch (Exception e) {            
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error!");                  
        }
    }

    @GetMapping(path="/docs/secure/render") 
    public @ResponseBody String secureRender (@RequestParam Map<String,String> data) {                
        try { 
            String url = data.get("url");
            URL newURL = new URL(url);
            String[] domains = {"raw.githubusercontent.com"};
            boolean validatedDomain = Arrays.stream(domains).anyMatch(newURL.getHost()::equals);            
            if (validatedDomain){                
                System.out.println(url);
                Docs docs = docsService.findByUrl(url);
                if (docs != null){                
                    CloseableHttpClient httpClient = HttpClients.createDefault();
                    HttpGet request = new HttpGet(docs.getUrl());
                    CloseableHttpResponse response = httpClient.execute(request);
                    HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    return result;                        
                }
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found!");                  
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request!");                          
        }
        catch (MalformedURLException e) {   
            e.printStackTrace();         
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error!");                  
        }
        catch (IOException e1) {   
            e1.printStackTrace();         
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error!");                  
        }
    }

    @PostMapping(path="/docs/secure/create") 
    public @ResponseBody Docs addSecureDocs (@RequestBody Docs docs) { 
        try {               
            String url = docs.getUrl();
            URL newURL = new URL(url);
            String[] domains = {"raw.githubusercontent.com"};
            boolean validatedDomain = Arrays.stream(domains).anyMatch(newURL.getHost()::equals);
            if (validatedDomain){                
                Docs newDocs = new Docs();
                newDocs.setName(docs.getName());
                newDocs.setUrl(url);                         
                docsService.save(newDocs);             
                return newDocs;
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request!");                          
        }
        catch (MalformedURLException e) { 
            e.printStackTrace();           
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error!");                  
        }
    }
}