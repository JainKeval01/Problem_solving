package BankMangement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class FileHandling {
    private HashMap<String,User> savedUsers=new HashMap<>();
    HashMap<String,User> readFile() {
        try {
           BufferedReader br = new BufferedReader(new FileReader("users.txt"));
           String line;
           while((line=br.readLine())!=null)
           {
                String[] data=line.split(",");
                String name=data[0];
                int password=Integer.parseInt(data[1]);
                int balance=Integer.parseInt(data[2]);
                savedUsers.put(name,new User(name,password,balance));
           }

        br.close();
        }catch (IOException e)
        {
            e.printStackTrace();

        }
        return savedUsers;
    }
    HashMap<String,User> updateList(HashMap<String,User> user){
        try {
            FileWriter fw = new FileWriter("users.txt");
            for(User u:user.values()){
                fw.write(u.getName()+","
                        +u.getBalance()+","+
                        u.getPassword());
                fw.write("\n");
            }
            fw.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return user;
    }
    HashMap<String,User> newAccount(String name,int balance,int password){
        try {
            FileWriter fw = new FileWriter("users.txt");
            fw.write(name+","+balance+","+password);
            savedUsers.put(name,new User(name,balance,password));
            updateList(savedUsers);
            fw.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return savedUsers;
    }
}
