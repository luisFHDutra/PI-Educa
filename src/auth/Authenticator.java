//package auth;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Scanner;
//import javax.swing.JOptionPane;
//
//public class Authenticator
//{
//    private ArrayList<? extends User> users;
//    private User loggedUser;
//
//    public Authenticator(ArrayList<? extends User> users)
//    {
//        this.users = users;
//        this.loggedUser = null;
//    }
//
//    public boolean showDialog(boolean withRepetition)
//    {
//        if (withRepetition)
//        {
//            boolean repeat = true;
//            while (repeat)
//            {
//                repeat = false;
//                showDialog();
//                if (!tela.isCanceled() && !isRight())
//                {
//                    JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos", "Autenticação", JOptionPane.ERROR_MESSAGE);
//                    repeat = true;
//                }
//            }
//        }
//        else
//        {
//            showDialog();
//        }
//        return (this.loggedUser != null);
//    }
//
//    public void showDialog()
//    {
//        tela.setVisible(true);
//    }
//
//    public boolean isRight()
//    {
//        boolean ok = false;
//        if (!tela.isCanceled() && !tela.getLogName().isEmpty() && !tela.getSenha().isEmpty())
//        {
//            for (User user : users)
//            {
//                if (user.getLogName().equals(tela.getLogName()))
//                {
//                    // Verifique a senha usando a API de verificação de senha
//                    if (checkPassword(tela.getSenha(), user.getHashCode()))
//                    {
//                        this.loggedUser = user;
//                        ok = true;
//                        break;
//                    }
//                }
//            }
//        }
//        return ok;
//    }
//
//    // Método para verificar a senha usando a API
//    public boolean checkPassword(String password, String hash) {
//        try {
//            URL url = new URL("https://www.toptal.com/developers/bcrypt/api/check-password.json");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//
//            // Construir o corpo da solicitação
//            String requestBody = "hash=" + hash + "&password=" + password;
//            try (OutputStream os = connection.getOutputStream()) {
//                byte[] input = requestBody.getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//
//            // Ler a resposta da API
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
//                    StringBuilder response = new StringBuilder();
//                    String responseLine;
//                    while ((responseLine = br.readLine()) != null) {
//                        response.append(responseLine.trim());
//                    }
//
//                    // Analisar a resposta JSON manualmente
//                    String jsonResponse = response.toString();
//                    boolean passwordChecked = parseJson(jsonResponse);
//
//                    // Verificar se a senha foi verificada com sucesso
//                    return passwordChecked;
//                }
//            } else {
//                // Lida com a resposta de erro da API de verificação de senha
//                System.err.println("Erro ao verificar a senha. Código de resposta: " + responseCode);
//                return false;
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false; // Lida com erros de forma apropriada, por exemplo, retornando false
//        }
//    }
//
//    // Função para analisar o JSON manualmente
//    private boolean parseJson(String json) {
//        // Implemente a análise do JSON manualmente com base na estrutura real da resposta JSON
//        // Neste exemplo, assumimos que a resposta JSON contém a chave "ok" com valor booleano
//        // Certifique-se de ajustar essa parte de acordo com a estrutura real da resposta JSON.
//        try {
//            int start = json.indexOf("\"ok\":") + 6;
//            int end = json.indexOf(",", start);
//            if (start != -1 && end != -1) {
//                String okValue = json.substring(start, end).trim();
//                return Boolean.parseBoolean(okValue);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
//
//    // Método para gerar o hash code através da API
//    public String generateHashCode(String senha) {
//        try {
//            URL url = new URL("https://www.toptal.com/developers/bcrypt/api/generate-hash.json");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setDoOutput(true);
//
//            // Construir o corpo da solicitação
//            String requestBody = "password=" + senha + "&cost=4"; // Substitua '4' pelo custo desejado
//            try (OutputStream os = connection.getOutputStream()) {
//                byte[] input = requestBody.getBytes("utf-8");
//                os.write(input, 0, input.length);
//            }
//
//            // Ler a resposta da API
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                try (InputStream inputStream = connection.getInputStream()) {
//                    String responseBody = new Scanner(inputStream, "utf-8").useDelimiter("\\A").next();
//
//                    // Analisar a resposta JSON usando o Scanner
//                    Scanner scanner = new Scanner(responseBody);
//                    scanner.useDelimiter("\\Z");
//                    String json = scanner.hasNext() ? scanner.next() : "";
//
//                    // Analisar a resposta JSON para obter o hash gerado
//                    String generatedHash = null;
//                    if (!json.isEmpty()) {
//                        // Remove qualquer caractere de quebra de linha que possa estar presente
//                        json = json.replaceAll("\\n", "");
//
//                        // Analisar a resposta JSON
//                        if (json.contains("\"ok\":true")) {
//                            int hashIndex = json.indexOf("\"hash\":\"");
//                            if (hashIndex != -1) {
//                                int endIndex = json.indexOf("\"", hashIndex + 8);
//                                if (endIndex != -1) {
//                                    generatedHash = json.substring(hashIndex + 8, endIndex);
//                                }
//                            }
//                        }
//                    }
//
//                    return generatedHash;
//                }
//            } else {
//                // Lida com a resposta de erro da API de geração de hash
//                System.err.println("Erro ao chamar a API de geração de hash. Código de resposta: " + responseCode);
//                return null;
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null; // Lida com erros de forma apropriada, por exemplo, retornando null
//        }
//    }
//    
//    public User getLoggedUser()
//    {
//        return loggedUser;
//    }
//}
