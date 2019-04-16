package com.schandorf.msaconstitution;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.Gson;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
final static String PATH = "constitution.json";
    final static String FONT_FILE = "Candara.ttf";
    TextView container;
    List<ConstitutionPOJO> constitutionPOJOList;
    TextView titleText;
    LinearLayout containerLayout;
    NestedScrollView scrollviewContainer;
    ImageView msaLogo;
    int articlePosition = 0;
    TextView msaText;
    TextView uhasText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FontChangeCrawler fontChangeCrawler = new FontChangeCrawler(getAssets(),FONT_FILE);
        fontChangeCrawler.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        constitutionPOJOList = getSource();
        Menu menu = navigationView.getMenu();
        addMenuItems(menu,constitutionPOJOList);
        navigationView.invalidate();

        View headerView = navigationView.getHeaderView(0);
        msaText = headerView.findViewById(R.id.msaText);
        uhasText = headerView.findViewById(R.id.uhasText);
        Typeface myFont = Typeface.createFromAsset(getAssets(),FONT_FILE);
        msaText.setTypeface(myFont);
        uhasText.setTypeface(myFont);
        changeMenuFont(menu);

        scrollviewContainer = findViewById(R.id.scrollViewContainer);
      //  containerLayout = findViewById(R.id.container_layout);
        container = findViewById(R.id.textcontainer);
        msaLogo = findViewById(R.id.msa_logo);
        container.setTextIsSelectable(true);
        titleText = findViewById(R.id.title_text);
        Intent fromIntent = getIntent();
        if(fromIntent.hasExtra("id"))
        {
            if(msaLogo.getVisibility() == View.VISIBLE)
            { msaLogo.setVisibility(View.GONE);}
            titleText.setText(createSpanned("<h4>"+constitutionPOJOList.get(fromIntent.getIntExtra("id",0)).getArticle()+" - "+constitutionPOJOList.get(fromIntent.getIntExtra("id",0)).getTitle()+"</h4>"));
            container.setText(createSpanned(constitutionPOJOList.get(fromIntent.getIntExtra("id",0)).getContent()));
            articlePosition = fromIntent.getIntExtra("id",0);
        }
        else
        {
//        titleText.setText(createSpanned("<h4>"+constitutionPOJOList.get(0).getArticle()+" - "+constitutionPOJOList.get(0).getTitle()+"</h4>"));
//        container.setText(createSpanned(constitutionPOJOList.get(0).getContent()));
            showAbout();
        }

        titleText.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){
            public void onSwipeTop() {
               // Toast.makeText(getApplicationContext(), "Top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                scrollviewContainer.scrollTo(0,0);
                //Toast.makeText(getApplicationContext(), "Right", Toast.LENGTH_SHORT).show();
                int index = getNextIndex(articlePosition,constitutionPOJOList);
                titleText.setText(createSpanned("<h4>"+constitutionPOJOList.get(index).getArticle()+" - "+constitutionPOJOList.get(index).getTitle()+"</h4>"));
                container.setText(createSpanned(constitutionPOJOList.get(index).getContent()));
                articlePosition = index;
            }
            public void onSwipeLeft() {
               // Toast.makeText(getApplicationContext(), "Left", Toast.LENGTH_SHORT).show();
                scrollviewContainer.scrollTo(0,0);
               int index = getPrevIndex(articlePosition,constitutionPOJOList);
                titleText.setText(createSpanned("<h4>"+constitutionPOJOList.get(index).getArticle()+" - "+constitutionPOJOList.get(index).getTitle()+"</h4>"));
                container.setText(createSpanned(constitutionPOJOList.get(index).getContent()));
                articlePosition = index;
            }
            public void onSwipeBottom() {
                //Toast.makeText(getApplicationContext(), "Bottom", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            //MainActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.action_search:
                Intent intent = new Intent(this,SearchActivity.class);
                intent.putExtra("list", (Serializable) constitutionPOJOList);
                startActivity(intent);
               // MainActivity.this.finish();
                break;
            case R.id.action_share:
                doShare();
                break;
            case R.id.action_about:
               showAbout();
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    private void showAbout()
    {
        String aboutText = "<p>&copy; 2018 University of Health and Allied Sciences , Medical Students' Association - UHAS MSA<br><br>" +
                "The purpose of this app is to ensure that the UHAS MSA constitution can be made portable</p>";
        String aboutContent = aboutText + "<p>" +
                "    <b><u>Developers:</u></b><br><br>\n" +
                "    <b>Name:</b> Osam-Frimpong Schandorf<br>\n" +
                "    <b>Tel:</b> +233547289638 / +233271289638<br>\n" +
                "   <b>E-mail:</b> osamfrimpong@gmail.com / osamfrimpong@yahoo.com<br>\n" +
                "    <b>Facebook:</b> @osamfrimpong<br>\n" +
                "    <b>Twitter:</b> @osamfrimpong\n" +
                "</p>" +
                "<p>" +
                "    <b>Name:</b> Agbesi Silas<br>\n" +
                "    <b>Tel:</b> +233249204110<br>\n" +
                "   <b>E-mail:</b> calculus389@gmail.com <br>\n" +
                "    <b>Facebook:</b> @silanovichjoy<br>\n" +
                "</p>";
        titleText.setVisibility(View.GONE);
        msaLogo.setVisibility(View.VISIBLE);
        container.setText(createSpanned(aboutContent));

            scrollviewContainer.smoothScrollTo(0,0);

    }

    private void doShare() {
        String fromContainer = container.getText().toString();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/html");
        shareIntent.putExtra(Intent.EXTRA_TEXT,fromContainer);
        startActivity(Intent.createChooser(shareIntent,"Share Article With"));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        articlePosition = id;
        if(msaLogo.getVisibility() == View.VISIBLE)
        { msaLogo.setVisibility(View.GONE);}

        if(titleText.getVisibility() == View.GONE)
        {titleText.setVisibility(View.VISIBLE);}
        titleText.setText(createSpanned("<h4>"+constitutionPOJOList.get(id).getArticle()+" - "+constitutionPOJOList.get(id).getTitle()+"</h4>"));
        container.setText(createSpanned(constitutionPOJOList.get(id).getContent()));
        if(scrollviewContainer.getY() > 0)
        {
            scrollviewContainer.smoothScrollTo(0,0);
        }
        //scrollviewContainer.scrollTo(0,0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Spanned createSpanned(String RawSource)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(RawSource,Html.FROM_HTML_MODE_LEGACY);
        }
        else
        {
            return Html.fromHtml(RawSource);
        }
    }

    private List<ConstitutionPOJO> getSource()
    {
        List<ConstitutionPOJO> constitutionItems;
        Gson gson= new Gson();

        String jsonString;
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(PATH)));
            while((jsonString = bufferedReader.readLine()) != null)
            {
                sb.append(jsonString);
            }

            Items items = gson.fromJson(sb.toString(),Items.class);
            bufferedReader.close();
            if(items != null)
            {
                 constitutionItems = items.items;


            }
            else
            {
                constitutionItems = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
           // Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT).show();
            constitutionItems = null;
        }

        return constitutionItems;
    }

    private void addMenuItems(Menu menu,List<ConstitutionPOJO> items)
    {
        int i = 0;
        for(ConstitutionPOJO cp : items)
        {
            menu.add(0,(cp.getId()-1),cp.getId(),cp.getArticle());

        }
    }

    private List<ConstitutionPOJO> searchResult(String searchTerm,List<ConstitutionPOJO> sourceList)
    {
        List<ConstitutionPOJO> results = new ArrayList<>();
        for (ConstitutionPOJO cpp : sourceList)
        {
           if(cpp.getTitle().contains(searchTerm) || cpp.getContent().contains(searchTerm))
           {
               results.add(cpp);
           }
        }
        return results;
    }

    private int getNextIndex(int currentIndex,List<ConstitutionPOJO> listOfItems)
    {
        if(currentIndex < listOfItems.size()-1)
        {
        return currentIndex + 1;}
        else
        {
            return 0;
        }
    }

    private int getPrevIndex(int currentIndex,List<ConstitutionPOJO> listOfItems)
    {
        if(currentIndex > 0)
        {
            return currentIndex - 1;}
        else
        {
            return listOfItems.size() - 1;
        }
    }

    private void changeMenuFont(Menu menu)
    {
        Typeface font = Typeface.createFromAsset(getAssets(),FONT_FILE);
        for(int i=0;i<menu.size();i++)
        {
            MenuItem menuItem = menu.getItem(i);
            SpannableString s = new SpannableString(menuItem.getTitle());
            s.setSpan(new CustomTypefaceSpan("",font),0,s.length(),Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            menuItem.setTitle(s);
        }
    }
}
