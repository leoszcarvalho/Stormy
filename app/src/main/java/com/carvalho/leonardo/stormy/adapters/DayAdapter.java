package com.carvalho.leonardo.stormy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.carvalho.leonardo.stormy.R;
import com.carvalho.leonardo.stormy.weather.Day;

/**
 * Created by Leonardo on 26/09/2015.
 */

//Esta classe é a responsável pela população dos dados da lista na View

public class DayAdapter extends BaseAdapter
{

    private Context mContext;
    private Day[] mDays;

    //O construtor retorna o contexto e o objeto
    public DayAdapter(Context context, Day[] days)
    {

        mContext = context;
        mDays = days;


    }

    //Esse método retorna o tamanho do array
    @Override
    public int getCount() {
        return mDays.length;
    }

    //Este método retorna o item da lista do objeto que será tratado
    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    //Método não utilizado
    @Override
    public long getItemId(int position) {
        return 0; // we aren't to use that. Tag itens for easy reference
    }


    //Pega a view do item da lista do arquivo de layout xml daily_list_item, depois instancia a classe ViewHolder pra popular as views da lista
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if(convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item,null);

            holder = new ViewHolder();

            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.temperatureLabel = (TextView) convertView.findViewById(R.id.temperatureLabel);

            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayNameLabel);

            convertView.setTag(holder);


        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        Day day = mDays[position];

        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(String.valueOf(day.getTemperatureMax()));

        if(position != 0)
        {

            holder.dayLabel.setText(day.getDayOfTheWeek());
        }
        else
        {
            holder.dayLabel.setText("Today");

        }

        return convertView;
    }

    //classe com as variáveis que serão passadas no holder pra popular as views
    private static class ViewHolder
    {
        ImageView iconImageView;
        TextView temperatureLabel;
        TextView dayLabel;
    }

}
