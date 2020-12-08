package com.example.esiapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.esiapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.example.esiapp.adapters.PostDetail.COMMENT_KEY;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private Context mContext;
    private List<Post> mData ;
    private String userId;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);
        return new MyViewHolder(row);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.time.setText(timestampToString((Long)mData.get(position).getimeStamp()));
        holder.tvTitle.setText(mData.get(position).getTitle());
        holder.tvDescription.setText(mData.get(position).getDescription());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);
        holder.userName.setText(mData.get(position).getUserName());
        like(mData.get(position).getPostKey(),holder.post_adapter_like,holder.likeText);
        likeNumber(holder.number_Likes,mData.get(position).getPostKey());
        holder.likeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.post_adapter_like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference()
                            .child("likes").child(mData.get(position).getPostKey()).child(userId).child("is like").setValue("true");
                }
                else {
                    FirebaseDatabase.getInstance().getReference()
                            .child("likes").child(mData.get(position).getPostKey()).child(userId).child("is like").removeValue();
                }
            }
        });
        comment_number(holder.comment_num,mData.get(position).getPostKey());

        //Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgPostProfile);
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvTitle, number_Likes, time, userName, tvDescription, likeText,comment_num;
        ImageView imgPost, post_adapter_like, imgPostProfile;
        ConstraintLayout likeContainer,comment;
        @RequiresApi(api = Build.VERSION_CODES.N)
        MyViewHolder(View itemView)
        {
            super(itemView);
            comment_num=itemView.findViewById(R.id.num_comment);
            tvTitle = itemView.findViewById(R.id.post_subject);
            tvDescription=itemView.findViewById(R.id.post_description);
            imgPost = itemView.findViewById(R.id.post_picture);
            time = itemView.findViewById(R.id.post_date);
            userName= itemView.findViewById(R.id.person_name);
            post_adapter_like=itemView.findViewById(R.id.post_adaper_like);
            number_Likes= itemView.findViewById(R.id.like_num);
            likeContainer = itemView.findViewById(R.id.post_adapter_like);
            likeText = itemView.findViewById(R.id.like_text);
            comment = itemView.findViewById(R.id.post_adapter_comment);
            imgPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postDetailActivity = new Intent(mContext, PostDetail.class);
                    int position = getAdapterPosition();
                    postDetailActivity.putExtra("userId",mData.get(position).getUserId());
                    postDetailActivity.putExtra("postImage",mData.get(position).getPicture());
                    postDetailActivity.putExtra("description",mData.get(position).getDescription());
                    postDetailActivity.putExtra("postKey",mData.get(position).getPostKey());
                    //postDetailActivity.putExtra("userPhoto",mData.get(position).getUserPhoto());
                    postDetailActivity.putExtra("userName",mData.get(position).getUserName());
                    long timestamp  = (long) mData.get(position).getTimeStamp();
                    postDetailActivity.putExtra("postDate",timestamp) ;
                    mContext.startActivity(postDetailActivity);
                }
            });
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postDetailActivity = new Intent(mContext, PostDetail.class);
                    int position = getAdapterPosition();
                    postDetailActivity.putExtra("userId",mData.get(position).getUserId());
                    postDetailActivity.putExtra("postImage",mData.get(position).getPicture());
                    postDetailActivity.putExtra("description",mData.get(position).getDescription());
                    postDetailActivity.putExtra("postKey",mData.get(position).getPostKey());
                    //postDetailActivity.putExtra("userPhoto",mData.get(position).getUserPhoto());
                    postDetailActivity.putExtra("userName",mData.get(position).getUserName());
                    long timestamp  = (long) mData.get(position).getTimeStamp();
                    postDetailActivity.putExtra("postDate",timestamp) ;
                    mContext.startActivity(postDetailActivity);
                }
            });
        }
    }
    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        return DateFormat.format("dd MMM yyyy à HH:mm ",calendar).toString();
    }
    private void like (String postid, final ImageView img, final TextView text) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userId).exists()) {
                    img.setImageResource(R.drawable.like_clickled);
                    img.setTag("liked");
                    text.setText("Liked");
                    text.setTextColor(Color.parseColor("#0090FF"));

                } else {
                    img.setImageResource(R.drawable.like);
                    img.setTag("like");
                    text.setText("Like");
                    text.setTextColor(Color.parseColor("#000000"));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void likeNumber (final TextView  likes, String postId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("likes").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(""+ dataSnapshot.getChildrenCount());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void comment_number (final TextView  num_comment, String postId) {
        DatabaseReference comment_ref = FirebaseDatabase.getInstance().getReference(COMMENT_KEY).child(postId);
        comment_ref.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                num_comment.setText(""+ dataSnapshot.getChildrenCount());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

