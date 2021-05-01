package mobi.graff.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private Button logOutBtn;
    private FirebaseUser user;
    private DatabaseReference reference;
    private TextView fullNameTextView, emailTextView, ageTextView, phoneTextView, addressTextView;

    private String userID;

    public ProfileFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        fullNameTextView = (TextView) view.findViewById(R.id.fullName);
        emailTextView = (TextView) view.findViewById(R.id.emailAddress);
        ageTextView = (TextView) view.findViewById(R.id.age);
        phoneTextView = (TextView) view.findViewById(R.id.phone);
        addressTextView = (TextView) view.findViewById(R.id.address);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null) {
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String age = userProfile.age;
                    String phone = userProfile.phone;
                    String address = userProfile.address;

                    fullNameTextView.setText(fullName);
                    emailTextView.setText(email);
                    ageTextView.setText(age);
                    phoneTextView.setText(phone);
                    addressTextView.setText(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

       /* logOutBtn = (Button) view.findViewById(R.id.btnLogout);
        logOutBtn.setOnClickListener((View.OnClickListener) this);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileFragment.this.getActivity(), MainActivity.class);
                ProfileFragment.this.startActivity(intent);

            }
        });


    }

   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                startActivity(new Intent(this, MainActivity.class));
        }
    }*/
        return view;
    }
}
