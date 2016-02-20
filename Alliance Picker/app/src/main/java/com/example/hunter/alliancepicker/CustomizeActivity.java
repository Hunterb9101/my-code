package com.example.elliott.alliancepicker;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CustomizeActivity extends AppCompatActivity {
    ColorScheme selectedColor = ColorScheme.selectedColor;
    int rowNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        for(int cntr=0; cntr< ColorScheme.allColorSchemes.toArray().length; cntr++){
            Customize_CreateRow(ColorScheme.allColorSchemes.get(cntr).desc);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customize, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Customize_CreateRow(String dispText) {
        TableLayout tl = (TableLayout)findViewById(R.id.customizeTable);

        ScrollView sv = (ScrollView)findViewById(R.id.customize);
        sv.setBackgroundColor(Color.parseColor(selectedColor.EdgeBgColor));

        TableRow tr1 = new TableRow(this);
        tr1.setId(rowNum);
        tr1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView textview = (TextView) getLayoutInflater().inflate(R.layout.results_row, null);

        String uri = "@drawable/cscheme_" + (rowNum+1);

        int imageResource = getResources().getIdentifier(uri, null, getPackageName());

        ImageView screenshot = (ImageView)getLayoutInflater().inflate(R.layout.customize_image,null);
        Drawable res = getResources().getDrawable(imageResource);
        screenshot.setImageDrawable(res);

        tr1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ColorScheme.selectedColor = ColorScheme.allColorSchemes.get(v.getId());
                Intent myIntent = new Intent(CustomizeActivity.this, MainActivity.class);
                CustomizeActivity.this.startActivity(myIntent);
                CustomizeActivity.this.finish();
            }
        });

        textview.setId(5300 + rowNum);
        textview.setTextColor(Color.parseColor(selectedColor.TxtColor));
        textview.setText(dispText);

        if (rowNum % 2 == 0) {tr1.setBackgroundColor(Color.parseColor(selectedColor.BgColor));}
        else {tr1.setBackgroundColor(Color.parseColor(selectedColor.BgColor2));}

        tr1.addView(screenshot);
        tr1.addView(textview);

        tl.addView(tr1, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rowNum++;
    }
}
