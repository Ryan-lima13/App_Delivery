package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rlds.appdelivery.R;

import java.util.ArrayList;
import java.util.List;

import Model.Produtos;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.ProdutoViewHolder> {
    private Context context;
    private List<Produtos>listaProdutos = new ArrayList<>();

    public AdapterProduto(Context context, List<Produtos> listaProdutos) {
        this.context = context;
        this.listaProdutos = listaProdutos;
    }

    @NonNull
    @Override
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        itemLista = layoutInflater.inflate(R.layout.produto_item, parent,false);

        return new ProdutoViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        Glide.with(context).load(listaProdutos.get(position).getFoto()).into(holder.circleImageViewFotoItem);
        holder.textViewNome.setText(listaProdutos.get(position).getNome());
        holder.textViewPreco.setText(listaProdutos.get(position).getPreco());




    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    public  class ProdutoViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewNome, textViewPreco, textViewDescricao;
        private CircleImageView circleImageViewFotoItem;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNome = itemView.findViewById(R.id.nomeProduto);
            textViewPreco = itemView.findViewById(R.id.preco);
            circleImageViewFotoItem = itemView.findViewById(R.id.fotoItem);
        }
    }
}
