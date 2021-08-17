package com.example.jorge.entregasadomicilio.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jorge.entregasadomicilio.R;
import com.example.jorge.entregasadomicilio.components.Activity_Entrega_Jefe;
import com.example.jorge.entregasadomicilio.database.BaseDeDatos;
import com.example.jorge.entregasadomicilio.model.Producto;

import java.util.ArrayList;


public class Adaptador_Producto extends RecyclerView.Adapter<Adaptador_Producto.MyHolder> {
    RecyclerTouchListener listener;

    public interface RecyclerTouchListener {
        void onClickItem(View v, int position);
    }

    Context context;
    LayoutInflater inflater;
    ArrayList<Producto> model;
    BaseDeDatos bd;
    long backPressedTime = 0;
    String idJefe;


    public Adaptador_Producto(Context context, ArrayList<Producto> model, String idJefe) {
        bd = new BaseDeDatos(context);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.model = model;
        this.idJefe = idJefe;
    }

    public int getItemCount() {
        return model.size();
    }

    @Override
    public Adaptador_Producto.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_producto, parent, false);
        return new Adaptador_Producto.MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final Adaptador_Producto.MyHolder holder, final int position) {
        final Producto producto = model.get(position);
        String nombre = producto.getNombre() + " (" + String.valueOf(producto.getCantidad()) + ")";
        holder.txt_nombre_producto.setText(nombre);
        holder.txt_cantidadProductos.setText(String.valueOf(producto.getCantidadAVender()));
        final String precio = String.valueOf(producto.getPrecio()) + "cup";
        holder.txt_precio.setText(precio);
        if (producto.getSeVendio().equals("SI")) {
            holder.imageSeVendio.setImageResource(R.drawable.img_ok1);
        } else {
            holder.imageSeVendio.setImageResource(R.drawable.img_not_ok);
        }
        String precioTotal = String.valueOf(producto.getPrecioPorCantidad()) + "cup";
        holder.precioTotal.setText(precioTotal);

        if (producto.getCantidadAVender() == 0) {
            holder.botonMas.setVisibility(View.VISIBLE);
            holder.botonMenos.setVisibility(View.GONE);
        }
        if (producto.getCantidadAVender() == producto.getCantidad()) {
            holder.botonMas.setVisibility(View.GONE);
            holder.botonMenos.setVisibility(View.VISIBLE);
        }

        holder.botonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (producto.getCantidadAVender() == 1) {
                    producto.setCantidadAVender(0);
                    producto.setPrecioTotal(0);
                    holder.txt_cantidadProductos.setText(String.valueOf(producto.getCantidadAVender()));
                    holder.precioTotal.setText("0 cup");
                    holder.botonMenos.setVisibility(View.GONE);
                    holder.botonMas.setVisibility(View.VISIBLE);
                }
                if (producto.getCantidadAVender() > 1) {
                    producto.setCantidadAVender(producto.getCantidadAVender() - 1);
                    holder.txt_cantidadProductos.setText(String.valueOf(producto.getCantidadAVender()));
                    String precioTotal = String.valueOf(producto.getPrecioPorCantidad()) + "cup";
                    holder.precioTotal.setText(precioTotal);
                    holder.botonMas.setVisibility(View.VISIBLE);
                }
                if (producto.getCantidadAVender() == 0) {
                    holder.botonMas.setVisibility(View.VISIBLE);
                    holder.botonMenos.setVisibility(View.GONE);
                }
                bd.actualizarProducto(producto.getId(), producto);
            }
        });

        holder.botonMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (producto.getCantidadAVender() == 0) {
                    producto.setCantidadAVender(1);
                    holder.txt_cantidadProductos.setText(String.valueOf(1));
                    String precioTotal = String.valueOf(producto.getPrecioPorCantidad()) + "cup";
                    holder.precioTotal.setText(precioTotal);
                    holder.botonMenos.setVisibility(View.VISIBLE);
                } else if (producto.getCantidadAVender() < producto.getCantidad()) {
                    producto.setCantidadAVender(producto.getCantidadAVender() + 1);
                    holder.txt_cantidadProductos.setText(String.valueOf(producto.getCantidadAVender()));
                    String precioTotal = String.valueOf(producto.getPrecioPorCantidad()) + "cup";
                    holder.precioTotal.setText(precioTotal);
                    holder.botonMenos.setVisibility(View.VISIBLE);
                }
                if (producto.getCantidadAVender() == producto.getCantidad()) {
                    holder.botonMas.setVisibility(View.GONE);
                    holder.botonMenos.setVisibility(View.VISIBLE);
                }
                bd.actualizarProducto(producto.getId(), producto);
            }
        });

        holder.botonEliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast backToast = Toast.makeText(context, "Presiona otra vez para eliminar el producto " + producto.getNombre(), Toast.LENGTH_LONG);
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backToast.cancel();
                    bd.eliminarProducto(producto.getId());
                    model.remove(position);
                    Intent intent = new Intent(context, Activity_Entrega_Jefe.class);
                    intent.putExtra("ID_ENTREGA", producto.getId_entrega());
                    intent.putExtra("ID_JEFE", idJefe);
                    intent.putExtra("PRODUCTO_ELIMINADO", "ok");
                    ((Activity_Entrega_Jefe) context).finish();
                    context.startActivity(intent);
                    return;
                } else {
                    backToast.show();
                }
                backPressedTime = System.currentTimeMillis();
            }
        });

        holder.botonEditarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Activity_Entrega_Jefe.class);
                intent.putExtra("ID_ENTREGA", producto.getId_entrega());
                intent.putExtra("ID_JEFE", idJefe);
                intent.putExtra("EDITAR_PRODUCTO", "OK");
                intent.putExtra("ID_PRODUCTO", producto.getId());
                ((Activity_Entrega_Jefe) context).finish();
                context.startActivity(intent);
            }
        });
    }

    public void setClickListener(Adaptador_Producto.RecyclerTouchListener value) {
        this.listener = value;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txt_nombre_producto, txt_cantidadProductos, txt_precio, precioTotal;
        ImageButton botonMenos, botonMas, botonEditarProducto, botonEliminarProducto;
        ImageView imageSeVendio;

        public MyHolder(final View view) {
            super(view);
            txt_nombre_producto = (TextView) view.findViewById(R.id.txt_nombre_producto);
            txt_cantidadProductos = (TextView) view.findViewById(R.id.txt_cantidadProductos);
            txt_precio = (TextView) view.findViewById(R.id.txt_precio);
            botonMenos = (ImageButton) view.findViewById(R.id.botonMenos);
            botonMas = (ImageButton) view.findViewById(R.id.botonMas);
            botonEditarProducto = (ImageButton) view.findViewById(R.id.botonEditarProducto);
            botonEliminarProducto = (ImageButton) view.findViewById(R.id.botonEliminarProducto);
            precioTotal = (TextView) view.findViewById(R.id.precioTotal);
            imageSeVendio = (ImageView) view.findViewById(R.id.imageSeVendio);

            view.setTag(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onClickItem(view, getAdapterPosition());
                }
            });
        }
    }

}
