package com.example.sarvesh.takeajob;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class ServerCon {

     static String uploadData(RequestPackage rp)
    {
        //Log.d("SERVERCON","In ServerCon.uploadData currently");
        URL url;
        BufferedReader reader=null;
        HttpURLConnection urlConnection=null;
        OutputStreamWriter writer=null;
        String uri=rp.getUri();
        try {
            //Log.d("SERVERCON","try block entry");
            url = new URL(uri);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(rp.getMethod());
            if (rp.getMethod().equals("GET"))
            {
                uri+= "?"+rp.getEncodedParams();
                Log.d("SERVERCON",uri);
            }
            else if (rp.getMethod().equals("POST"))
            {
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(10000);
                //this allows us to write some content in the body of request
                //JSONObject object=new JSONObject(rp.getParams()); // we can also send request in json Format instead of encoded format
                //String params="params="+object.toString();
                // Log.d("SERVER",object.toString());

                //Log.d("SERVERCON","POST method 1");
                OutputStream out_stream=urlConnection.getOutputStream();
                //Log.d("SERVERCON","POST method 2");
                writer=new OutputStreamWriter(out_stream);
                // writer.write(object.toString());
                //Log.d("SERVERCON","POST method 3");

                writer.write(rp.getEncodedParams());//it gets the encoded params exactly as GET request
                //Log.d("SERVERCON","POST method 4");
                writer.flush();
                out_stream.close();

            }

            InputStream is=urlConnection.getInputStream();
            reader=new BufferedReader(new InputStreamReader(is));

            String line;
            StringBuilder sb=new StringBuilder();
            while ((line=reader.readLine())!=null)
            {
                sb.append(line);
            }
            Log.d("SERVERCON","Exiting UploadData "+sb);

            return sb.toString();
        }
        catch (IOException e) {
            Log.d("SERVERCON","IOException in UploadData");
            e.printStackTrace();
        }

        catch (Exception e) {
            Log.d("SERVERCON","Exception in UploadData");
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                if (writer!=null)
                {
                    try {

                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            urlConnection.disconnect();
        }
        //Log.d("SERVER","UploadData returning null");
        return null;

    }
    public static List<String> fetchData(RequestPackage rp)
    {
        HttpURLConnection urlConnection=null;
        InputStream is;
        BufferedReader reader=null;
        OutputStreamWriter writer;
        String uri=rp.getUri();
        try {
            if (rp.getMethod().equals("GET"))
            {
                uri+= "?"+rp.getEncodedParams();
                //Log.d("SERVER","fetchData "+uri);
            }
            if (rp.getMethod().equals("POST"))
            {
                urlConnection.setDoOutput(true); //this allows us to write some content in the body of request
                writer=new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(rp.getEncodedParams());//it gets the encoded params exactly as GET request
                writer.flush();
            }
            URL url = new URL(uri);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            //Log.d("SERVER","breakpoint 1");

            is = urlConnection.getInputStream();
            //Log.d("SERVER","breakpoint 2");
            reader = new BufferedReader(new InputStreamReader(is));
            Log.d("SERVER","breakpoint 3");
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                //Log.d("SERVER","Line="+line.toString());
                sb.append(line);
            }

            //Log.d("SERVER","onPostExecute "+sb.toString());
            List<String> Info= parseEmpJson(sb.toString());

            //Log.d("SERVER",empInfo.toString());
            return Info;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("SERVER", " ");
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            urlConnection.disconnect();
        }


        return null;

    }
    static ArrayList<AdapterData> fetchJobData(RequestPackage rp)
    {
        Log.d("SERVER","currently in fetchjobData");
        HttpURLConnection urlConnection=null;
        InputStream is;
        BufferedReader reader=null;
        OutputStreamWriter writer;
        String uri=rp.getUri();
        try {
            if (rp.getMethod().equals("GET"))
            {Log.d("SERVER","currently in try GET");
                uri+="?"+ rp.getEncodedParams();
                Log.d("SERVER","fetchJobData "+uri);
            }
            if (rp.getMethod().equals("POST"))
            {
                urlConnection.setDoOutput(true); //this allows us to write some content in the body of request
                writer=new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(rp.getEncodedParams());//it gets the encoded params exactly as GET request
                writer.flush();
            }
            URL url = new URL(uri);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(20000);
            urlConnection.setReadTimeout(50000);
            Log.d("SERVER","breakpoint 0");
            urlConnection.connect();
            Log.d("SERVER","breakpoint 1");

            is = urlConnection.getInputStream();
            Log.d("SERVER","breakpoint 2");
            reader = new BufferedReader(new InputStreamReader(is));
            Log.d("SERVER","breakpoint 3");
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                //Log.d("SERVER","Line="+line.toString());
                sb.append(line);
            }

            Log.d("SERVER","onPostExecute "+sb.toString());
            ArrayList<AdapterData> Info= parseJobJson(sb.toString());

            //Log.d("SERVER",Info.toString());
            return Info;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("SERVER", "failed ");
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            urlConnection.disconnect();
        }
        return null;
    }
    public static List<String> fetchProfileData(RequestPackage rp)
    {
        HttpURLConnection urlConnection=null;
        InputStream is;
        BufferedReader reader=null;
        OutputStreamWriter writer;
        String uri=rp.getUri();
        try {
            if (rp.getMethod().equals("GET"))
            {
                Log.d("SERVER","Inside fetchProfileData");
                uri+= "?"+rp.getEncodedParams();
                Log.d("SERVER","fetchProfileData "+uri);
            }
            if (rp.getMethod().equals("POST"))
            {
                urlConnection.setDoOutput(true); //this allows us to write some content in the body of request
                writer=new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(rp.getEncodedParams());//it gets the encoded params exactly as GET request
                writer.flush();
            }
            URL url = new URL(uri);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.connect();
            Log.d("SERVER","breakpoint 1");

            is = urlConnection.getInputStream();
            Log.d("SERVER","breakpoint 2");
            reader = new BufferedReader(new InputStreamReader(is));
            Log.d("SERVER","breakpoint 3");
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                Log.d("SERVER","Line="+line.toString());
                sb.append(line);
            }

            Log.d("SERVER","onPostExecute "+sb.toString());
            List<String> Info= parseProfileJson(sb.toString());
            if(Info!=null) {
                Log.d("SERVER", Info.toString());
                return Info;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("SERVER", " ");
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            urlConnection.disconnect();
        }

        return null;

    }
    static String saveJob(RequestPackage rp)
    {
        Log.d("SERVERCON","In ServerCon.saveJob currently");
        URL url;
        BufferedReader reader=null;
        HttpURLConnection urlConnection=null;
        OutputStreamWriter writer=null;
        String uri=rp.getUri();
        try {Log.d("SERVERCON","try block entry");
            url = new URL(uri);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(rp.getMethod());
            if (rp.getMethod().equals("GET"))
            {
                uri+= "?"+rp.getEncodedParams();
                Log.d("SERVERCON",uri);
            }
            else if (rp.getMethod().equals("POST"))
            {
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(10000);
                //this allows us to write some content in the body of request
                //JSONObject object=new JSONObject(rp.getParams()); // we can also send request in json Format instead of encoded format
                //String params="params="+object.toString();
                // Log.d("SERVER",object.toString());

                Log.d("SERVERCON","POST method 1");
                OutputStream out_stream=urlConnection.getOutputStream();
                Log.d("SERVERCON","POST method 2");
                writer=new OutputStreamWriter(out_stream);
                // writer.write(object.toString());
                Log.d("SERVERCON","POST method 3");

                writer.write(rp.getEncodedParams());
                Log.d("SERVERCON","POST method 4");
                writer.flush();
                out_stream.close();

            }

            InputStream is=urlConnection.getInputStream();
            reader=new BufferedReader(new InputStreamReader(is));

            String line;
            StringBuilder sb=new StringBuilder();
            while ((line=reader.readLine())!=null)
            {
                sb.append(line);
            }
            Log.d("SERVERCON","Exiting saveJob");

            return sb.toString();
        }
        catch (IOException e) {
            Log.d("SERVERCON","IOException in saveJob");
            e.printStackTrace();
        }

        catch (Exception e) {
            Log.d("SERVERCON","Exception in saveJob");
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                if (writer!=null)
                {
                    try {

                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            urlConnection.disconnect();
        }
        //Log.d("SERVER","UploadData returning null");
        return null;

    }
    static String applyJob(RequestPackage rp)
    {
        Log.d("SERVERCON","In ServerCon.applyJob currently");
        URL url;
        BufferedReader reader=null;
        HttpURLConnection urlConnection=null;
        OutputStreamWriter writer=null;
        String uri=rp.getUri();
        try {Log.d("SERVERCON","try block entry");
            url = new URL(uri);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(rp.getMethod());
            if (rp.getMethod().equals("GET"))
            {
                uri+= "?"+rp.getEncodedParams();
                Log.d("SERVERCON",uri);
            }
            else if (rp.getMethod().equals("POST"))
            {
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(10000);
                //this allows us to write some content in the body of request
                //JSONObject object=new JSONObject(rp.getParams()); // we can also send request in json Format instead of encoded format
                //String params="params="+object.toString();
                // Log.d("SERVER",object.toString());

                Log.d("SERVERCON","POST method 1");
                OutputStream out_stream=urlConnection.getOutputStream();
                Log.d("SERVERCON","POST method 2");
                writer=new OutputStreamWriter(out_stream);
                // writer.write(object.toString());
                Log.d("SERVERCON","POST method 3");

                writer.write(rp.getEncodedParams());
                Log.d("SERVERCON","POST method 4");
                writer.flush();
                out_stream.close();

            }

            InputStream is=urlConnection.getInputStream();
            reader=new BufferedReader(new InputStreamReader(is));

            String line;
            StringBuilder sb=new StringBuilder();
            while ((line=reader.readLine())!=null)
            {
                sb.append(line);
            }
            Log.d("SERVERCON","Exiting applyJob");

            return sb.toString();
        }
        catch (IOException e) {
            Log.d("SERVERCON","IOException in applyJob");
            e.printStackTrace();
        }

        catch (Exception e) {
            Log.d("SERVERCON","Exception in applyJob");
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                if (writer!=null)
                {
                    try {

                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            urlConnection.disconnect();
        }
        //Log.d("SERVER","UploadData returning null");
        return null;

    }
    static String uploadFeed(RequestPackage rp)
    {
        Log.d("SERVERCON","In ServerCon.uploadFeed currently");
        URL url;
        BufferedReader reader=null;
        HttpURLConnection urlConnection=null;
        OutputStreamWriter writer=null;
        String uri=rp.getUri();
        try {Log.d("SERVERCON","try block entry");
            url = new URL(uri);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(rp.getMethod());
            if (rp.getMethod().equals("GET"))
            {
                uri+= "?"+rp.getEncodedParams();
                Log.d("SERVERCON",uri);
            }
            else if (rp.getMethod().equals("POST"))
            {
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(10000);
                //this allows us to write some content in the body of request
                //JSONObject object=new JSONObject(rp.getParams()); // we can also send request in json Format instead of encoded format
                //String params="params="+object.toString();
                // Log.d("SERVER",object.toString());

                Log.d("SERVERCON","POST method 1");
                OutputStream out_stream=urlConnection.getOutputStream();
                Log.d("SERVERCON","POST method 2");
                writer=new OutputStreamWriter(out_stream);
                // writer.write(object.toString());
                Log.d("SERVERCON","POST method 3");

                writer.write(rp.getEncodedParams());//it gets the encoded params exactly as GET request
                Log.d("SERVERCON","POST method 4");
                writer.flush();
                out_stream.close();

            }

            InputStream is=urlConnection.getInputStream();
            reader=new BufferedReader(new InputStreamReader(is));

            String line;
            StringBuilder sb=new StringBuilder();
            while ((line=reader.readLine())!=null)
            {
                sb.append(line);
            }
            Log.d("SERVERCON","Exiting uploadFeed");

            return sb.toString();
        }
        catch (IOException e) {
            Log.d("SERVERCON","IOException in uploadFeed");
            e.printStackTrace();
        }

        catch (Exception e) {
            Log.d("SERVERCON","Exception in uploadFeed");
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                if (writer!=null)
                {
                    try {

                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            urlConnection.disconnect();
        }
        //Log.d("SERVER","UploadData returning null");
        return null;

    }

    public static ArrayList<AdapterData> parseJobJson(String content)
    {

        try {
            JSONArray ar=new JSONArray(content);
                JSONArray array = ar.getJSONArray(0);

                Log.d("SERVER", "inside parseJobJson()"+array.toString());
                ArrayList<AdapterData> adapterDatas=new ArrayList<>();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Log.d("SERVER", "parseJobJson()"+object.toString());
                    ArrayList<String> info=new ArrayList<>();
                    info.add(object.getString("job_type"));
                    info.add(object.getString("post_date"));
                    info.add(object.getString("job_title"));
                    info.add(object.getString("experience"));
                    info.add(object.getString("qualification"));
                    info.add(object.getString("company_name"));
                    info.add(object.getString("head_officeCity"));
                    info.add(object.getString("job_desc"));
                    info.add(object.getString("min_salary"));
                    info.add(object.getString("max_salary"));
                    info.add(object.getString("Job_Code"));
                    adapterDatas.add(new AdapterData(info));
                    Log.d("SERVER","onPostexecute "+adapterDatas.get(i).toString());
                }

                return adapterDatas;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static List<String> parseProfileJson(String content)
    {
        if(!content.contains("failure")) {
            try {
                JSONArray ar = new JSONArray(content);
                JSONArray array = ar.getJSONArray(0);

                Log.d("SERVER", array.toString());

                ArrayList<String> info = new ArrayList<>();
                for (int i = 0; i < ar.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Log.d("SERVER", object.toString());

                    info.add(object.getString("emp_name"));
                    info.add(object.getString("age"));
                    info.add(object.getString("emp_desg"));
                    info.add(object.getString("emp_number"));
                    info.add(object.getString("emp_cmpName"));
                    info.add(object.getString("experience"));
                }
                return info;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private static List<String> parseEmpJson(String content)
    {

        try {
            JSONArray ar=new JSONArray(content);
            JSONArray array=ar.getJSONArray(0);

            Log.d("SERVER",array.toString());

            ArrayList<String> info=new ArrayList<>();
            for (int i=0;i<ar.length();i++)
            {
                JSONObject object=array.getJSONObject(i);
                Log.d("SERVER",object.toString());

                info.add(object.getString("loginid"));
                info.add(object.getString("user"));
                info.add(object.getString("pass"));
                info.add(object.getString("email"));
            }
            return info;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
