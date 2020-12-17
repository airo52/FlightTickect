package com.company;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.logging.Logger;

import java.io.*;
//import java.lang.reflect.Parameter;
//import java.net.URI;
import java.net.URLDecoder;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
//import java.applet.Applet;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
/*
import com.sun.net.httpserver.HttpExchange;
import org.json.simple.parser.JSONParser;
import java.util.spi.AbstractResourceBundleProvider;

import com.sun.net.httpserver.HttpHandler;
import com.sun.tools.jconsole.JConsoleContext;
import org.json.simple.JSONArray;

 */

import org.json.simple.JSONObject;



public class Main {
    private static final String DOCUMENT_ROOT = System.getProperty("user.dir");//user.dir
    private static final int PORT = 8000;

    private static boolean DEBUG;

    public static void main(String[] args) {
        DEBUG = Boolean.valueOf(System.getProperty("DEBUG", "false"));

        try (ServerSocket server = new ServerSocket(PORT)) {

            log("listening on port " + server.getLocalPort());

            int threadCount = 0;
            while (true) {
                Socket client = server.accept();
                ConnectionThread thread = new ConnectionThread(client, threadCount);
                thread.start();
                threadCount++;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String message) {
        System.out.println(message);

    }

  public static class ConnectionThread extends Thread {
        private Socket client;
        private int counter;

        public ConnectionThread(Socket client, int counter) {
            this.client = client;
            this.counter = counter;
        }

        public void run() {
            if (DEBUG) {
                String clientIP = client.getInetAddress().toString();
                int clientPort = client.getPort();
                log("Connection " + counter + " : connected from " + clientIP +
                        " with port " + clientPort + ".");
            }
            try (InputStream input = client.getInputStream();
                 OutputStream output = client.getOutputStream()) {

                String url = getRequestUrl(input,output);
                if (url == null) {
                    return;
                }
                if(url == "ok"){
                    return;
                }
                System.out.println(url);
                if (url == "fetch") {
                    sendHeader(output);
                    sendFlightList(output);

                    //System.out.println("fetching");
                }
               else if(url == "customers"){
                     sendHeader(output);
                     sendCustomers(output);
                }

                else {
                    String responseFilePath = DOCUMENT_ROOT + url;
                    File file = new File(responseFilePath);
                    if (!file.exists()) {
                        sendNotFound(output);
                        return;
                    }
                    String name = "/";

                    sendHeader(output);
                    sendFile(output, file, name);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String getRequestUrl(InputStream stream,OutputStream output) {
            Scanner reader = (new Scanner(stream)).useDelimiter("\n");

            if (!reader.hasNext()) {

                return null;
            }

            String line = reader.next();
            String url = line.split(" ")[1];
            if (DEBUG) {
                while (reader.hasNext()) {
                    log(line);
                    line = reader.next();
                }
            }
            if(url.endsWith("/admin")){
                System.out.println("admin");
                //return url + "index.html";
            }
            if (url.endsWith("/")) {
                return url + "index.html";
                // System.out.println("ends with");
            } else if (url.endsWith("/demo")) {
                //System.out.println("ok");
                return "fetch";
            } else if (url.endsWith("/customers")) {
                return "customers";
            }
            else if(url.indexOf("?") == -1) {
                  return url;
                   // System.out.println(Arrays.toString(value));
                }else{
                //HttpExchange http;
                String[] parts =url.split("\\?");
                Map<String, String> params = queryToMap(url);
                System.out.println("param A=" + params.get("admin") +""+params.get("kk"));

  if(params.get("username") !=null && params.get("flight") !=null && params.get("lastname") !=null){
      //book flight
sendHeader(output);


      Random rand = new Random(); //instance of random class
      int upperbound = 25;
      //generate random values from 0-24
      int id = rand.nextInt(upperbound);
      bookFlight(output,params.get("username"),params.get("flight"),params.get("lastname"),params.get("Origin"),params.get("depature"),params.get("Time"),id);
  }

  if(params.get("FlightNumber") !=null && params.get("Origin") !=null){
      Random rand = new Random(); //instance of random class
      int upperbound = 25;
      //generate random values from 0-24
      int id = rand.nextInt(upperbound);
      sendHeader(output);

      AddFlight(output,id,params.get("FlightNumber"),params.get("Origin"),params.get("Destination"),params.get("Depature"),params.get("Time"));

  }
  //Deleting user

  if(params.get("RemoveCustomer") !=null){
      sendHeader(output);
      RemoveUser(output,params.get("RemoveCustomer"));
  }

  if(params.get("RemoveFlight") !=null){
      sendHeader(output);
     // System.out.println(params.get("RemoveFlight"));
      RemoveFlight(output,params.get("RemoveFlight"));
  }

  if(params.get("user")!=null && params.get("password") !=null){
      sendHeader(output);
      LoginAdmin(output,params.get("user"),params.get("password"));
      System.out.println(params.get("user"));
  }
  if(params.get("detail") !=null){
      sendHeader(output);
     sendFlight(output,params.get("detail"));
      System.out.println(params.get("detail"));
  }

      return "ok";

            }

        }

        private void sendNotFound(OutputStream output) {
            PrintStream out = new PrintStream(output);
            out.println("HTTP/1.0 404 Not Found");
            out.println("");
            out.println("NOT FOUND");
        }

        private void sendHeader(OutputStream output) {
            PrintStream out = new PrintStream(output);
            out.println("HTTP/1.0 200 OK");
            out.println("MIME_version:1.0");
            out.println("Content_Type:text/htm1");
            out.println("");
        }

        private void sendFile(OutputStream output, File file, String path) throws IOException {
            try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {

                int len = (int) file.length();
                byte buf[] = new byte[len];
                in.readFully(buf);

                output.write(buf, 0, len);
            }
        }

        private void sendFlightList(OutputStream output) {
            PrintStream out = new PrintStream(output);


            File file = new File("out/production/httpServer/com/company/flights.txt");
            Scanner in = null;

           try {
                in = new Scanner(file);
                int i=0;
                String[] data;
               JSONObject obj = new JSONObject();
                while (in.hasNext()) {
                    String line = in.nextLine();

                    String[] tokens = line.split("::");


                    obj.put(tokens[0]+i,tokens[1]);
                    obj.put(tokens[2]+i,tokens[3]);
                    obj.put(tokens[4]+i,tokens[5]);
                    obj.put(tokens[6]+i,tokens[7]);
                    obj.put(tokens[8]+i,tokens[9]);
                    obj.put(tokens[10]+i,tokens[11]);




                    if(i == obj.size()){

                        System.out.println(i +""+line.length());
                    }

               i++;

                }

  out.print(obj);

           }

            catch(FileNotFoundException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

private void AddFlight(OutputStream output,int id,String FlightNumber,String Origin,String Destination,String Depature,String Time){
    PrintStream out = new PrintStream(output);
   // id::25::Number::axh77::Origin::Nairobi::Destination::Paris::Depature::29/09/2020::Time::11:00P.M
    try {
        FileWriter myWriter = new FileWriter("out/production/httpServer/com/company/flights.txt",true);
        myWriter.append("\n");
        myWriter.append("id::"+id+"::Number::"+FlightNumber+"::Origin::"+Origin+"::Destination::"+Destination+"::Depature::"+Depature+"::Time::"+Time);
        myWriter.close();
        out.println("Flight added.");
    } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }

}

private void bookFlight(OutputStream output,String username,String flight,String lastname,String Origin,String Depature,String Time,int id){
    PrintStream out = new PrintStream(output);
    try {
        FileWriter myWriter = new FileWriter("out/production/httpServer/com/company/booking.txt",true);
        myWriter.append("\n");
        myWriter.append("id::"+id+"::username::"+username+"::lastname::"+lastname+"::flight::"+flight+"::Origin::"+Origin+"::Depature::"+Depature+"::Time::"+Time);
        myWriter.close();
        out.println("Successfully booked.");
    } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }


}

      private void sendFlight(OutputStream output,String id) {
          PrintStream out = new PrintStream(output);
          File file = new File("out/production/httpServer/com/company/flights.txt");
          Scanner in = null;
          try {
              in = new Scanner(file);
              int i=0;
              JSONObject obj = new JSONObject();
              while (in.hasNext()) {
                  String line = in.nextLine();

                      if (line.contains(id)) {
                          String[] tokens = line.split("::");
                          obj.put(tokens[0]+i,tokens[1]);
                          obj.put(tokens[2]+i,tokens[3]);
                          obj.put(tokens[4]+i,tokens[5]);
                          obj.put(tokens[6]+i,tokens[7]);
                          obj.put(tokens[8]+i,tokens[9]);
                          obj.put(tokens[10]+i,tokens[11]);

                          //  System.out.println(tokens[2]);
                          // System.out.println(line +"is a valid course number  "+ (topics[i][1]));
                          break;
                      }
                      else{
                          i++;
                      }


              }
              System.out.println(obj);
              out.print(obj);
          } catch(FileNotFoundException e){
              // TODO Auto-generated catch block
              e.printStackTrace();
          }



      }

        public static Map<String, String> queryToMap(String query) {
            Map<String, String> result = new HashMap<>();
            for (String params : query.split("\\?")) {
                for (String param : params.split("&")) {
                    String[] entry = param.split("=");
                    if (entry.length > 1) {
                        result.put(entry[0], entry[1]);
                    } else {
                        result.put(entry[0], "");
                    }
                }
            }
            return result;
        }

 public void LoginAdmin(OutputStream output,String username,String password){
     PrintStream out = new PrintStream(output);
     File file = new File("out/production/httpServer/com/company/admin.txt");
     Scanner in = null;
     try {
         in = new Scanner(file);
         int i=0;
         JSONObject obj = new JSONObject();
         while (in.hasNext()) {
             String line = in.nextLine();

             if (line.contains(username)) {
                 String[] tokens = line.split("::");
                 System.out.println(tokens[1]);

                 if(tokens[3].equals(password)) {
                     out.print("success");
                 }
                 else{ out.print("wrong password");}
                 //  System.out.println(tokens[2]);
                 // System.out.println(line +"is a valid course number  "+ (topics[i][1]));
                 break;
             }

             else{

                 i++;
             }


         }

         System.out.println(obj);

     } catch(FileNotFoundException e){
         // TODO Auto-generated catch block
         e.printStackTrace();
     }


 }

      public void RemoveFlight(OutputStream output,String id){
          System.out.println(id);
          PrintStream outi = new PrintStream(output);
          File file = new File("out/production/httpServer/com/company/flights.txt");
          try {


              //  File file = new File("myFile.txt");
              File temp = new File("_temp_");
              PrintWriter out = new PrintWriter(new FileWriter(temp));

              Files.lines(file.toPath())
                      .filter(line -> !line.contains(id))
                      .forEach(out::println);
              out.flush();
              out.close();

              temp.renameTo(file);
              outi.println("removed");
          }
          catch (IOException ex) {
              ex.printStackTrace();
          }
      }

 private void  RemoveUser(OutputStream output,String id){
     PrintStream outi = new PrintStream(output);
   File file = new File("out/production/httpServer/com/company/booking.txt");
   try {


       //  File file = new File("myFile.txt");
       File temp = new File("_temp_");
       PrintWriter out = new PrintWriter(new FileWriter(temp));

       Files.lines(file.toPath())
               .filter(line -> !line.contains(id))
               .forEach(out::println);
       out.flush();
       out.close();

       temp.renameTo(file);
       outi.println("removed");
   }
   catch (IOException ex) {
       ex.printStackTrace();
   }

 }
      private void sendCustomers(OutputStream output) {

          PrintStream out = new PrintStream(output);


          File file = new File("out/production/httpServer/com/company/booking.txt");
          Scanner in = null;

          try {
              in = new Scanner(file);
              int i=0;
              String[] data;
              JSONObject obj = new JSONObject();
              while (in.hasNext()) {
                  String line = in.nextLine();

                  String[] tokens = line.split("::");
                  System.out.println(Arrays.toString(tokens));

                  obj.put(tokens[0]+i,tokens[1]);
                  obj.put(tokens[2]+i,tokens[3]);
                  obj.put(tokens[4]+i,tokens[5]);
                  obj.put(tokens[6]+i,tokens[7]);
                  obj.put(tokens[8]+i,tokens[9]);
                  obj.put(tokens[10]+i,tokens[11]);




                  if(i == obj.size()){

                      System.out.println(i +""+line.length());
                  }

                  i++;

              }

              out.print(obj);

          }

          catch(FileNotFoundException e){
              // TODO Auto-generated catch block
              e.printStackTrace();
          }

      }


    }

}
