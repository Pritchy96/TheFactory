package com.GenericStudios.TheCandyFactory.android;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.GenericStudios.TheCandyFactory.ActionResolver;
import com.GenericStudios.TheCandyFactory.Manager;

public class AndroidLauncher extends AndroidApplication implements GameHelperListener, ActionResolver {

	private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-4452611104193317/8256044384";
	//private static final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/developer?id=TheInvader360";
	//private static final String GITHUB_URL = "https://github.com/TheInvader360";
	//private static final String BLOG_URL = "http://theinvader360.blogspot.co.uk/";
	protected View gameView;
	private InterstitialAd interstitialAd;
	private GameHelper gameHelper;
	

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;

		initialize(new Manager(this), config);
		
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
			
	    if (gameHelper == null) {
	        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
	        gameHelper.enableDebugLog(true);
	      }
	      gameHelper.setup(this);
	  

		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {

				//Toast.makeText(getApplicationContext(), "Finished Loading Interstitial", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onAdClosed() {
				//Toast.makeText(getApplicationContext(), "Closed Interstitial", Toast.LENGTH_SHORT).show();
			}
		});

		loginGPGS();
	}

	@Override
	public void LoadInterstital() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					if (!interstitialAd.isLoaded()) {
						AdRequest interstitialRequest = new AdRequest.Builder().build();
						interstitialAd.loadAd(interstitialRequest);
						//Toast.makeText(getApplicationContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
					}
				}
			});
		} catch (Exception e) {
		}
	}

	@Override
	public void ShowInterstital() {
		runOnUiThread(new Runnable() {
			public void run() {
				if (interstitialAd.isLoaded()) {
					interstitialAd.show();
				}
				else {
					//Log.d(TAG, "Interstitial ad is not loaded yet");
				}
			}
		});		
	}

	@Override
	public boolean getSignedInGPGS() {
		// TODO Auto-generated method stub
		return gameHelper.isSignedIn();
		//return false;
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable(){
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	}

	@Override
	public void submitScoreGPGS(int score) {
		// TODO Auto-generated method stub
			
		if (gameHelper.isSignedIn()) 
		{
			
			Games.Leaderboards.submitScore(gameHelper.getApiClient(), "CgkI17WurtcMEAIQAg", score);
			
		}
		else if (!gameHelper.isConnecting()) 
		{
			loginGPGS();
		}
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getLeaderboardGPGS() {
		  if (gameHelper.isSignedIn()) {
			  	startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), "CgkI17WurtcMEAIQAg"), 100);
			  }
			  else if (!gameHelper.isConnecting()) {
			    loginGPGS();
			  }
	}

	@Override
	public void getAchievementsGPGS() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
		
	}
}



/*


package com.theinvader360.tutorial.libgdx.google.ads;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AndroidApplication implements ActionResolver {

  private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-6916351754834612/3808499421";
  private static final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/developer?id=TheInvader360";
  private static final String GITHUB_URL = "https://github.com/TheInvader360";
  private static final String BLOG_URL = "http://theinvader360.blogspot.co.uk/";
  protected View gameView;
  private InterstitialAd interstitialAd;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
    cfg.useGL20 = false;
    cfg.useAccelerometer = false;
    cfg.useCompass = false;

    // Do the stuff that initialize() would do for you
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

    RelativeLayout layout = new RelativeLayout(this);
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    layout.setLayoutParams(params);

    View gameView = createGameView(cfg);
    layout.addView(gameView);

    setContentView(layout);

    interstitialAd = new InterstitialAd(this);
    interstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
    interstitialAd.setAdListener(new AdListener() {
      @Override
      public void onAdLoaded() {
        Toast.makeText(getApplicationContext(), "Finished Loading Interstitial", Toast.LENGTH_SHORT).show();
      }
      @Override
      public void onAdClosed() {
        Toast.makeText(getApplicationContext(), "Closed Interstitial", Toast.LENGTH_SHORT).show();
      }
    });
  }

  private View createGameView(AndroidApplicationConfiguration cfg) {
    gameView = initializeForView(new AdTutorial(this), cfg);
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
    params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
    gameView.setLayoutParams(params);
    return gameView;
  }





  public void LoadInterstital() {
    try {
      runOnUiThread(new Runnable() {
        public void run() {
          if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            Toast.makeText(getApplicationContext(), "Showing Interstitial", Toast.LENGTH_SHORT).show();
          }
          else {
            AdRequest interstitialRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(interstitialRequest);
            Toast.makeText(getApplicationContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
          }
        }
      });
    } catch (Exception e) {
    }
  }















































  @Override
  public void onBackPressed() {
    final Dialog dialog = new Dialog(this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

    LinearLayout ll = new LinearLayout(this);
    ll.setOrientation(LinearLayout.VERTICAL);

    Button b1 = new Button(this);
    b1.setText("Quit");
    b1.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        finish();
      }
    });
    ll.addView(b1);

    Button b2 = new Button(this);
    b2.setText("Games");
    b2.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_URL)));
        dialog.dismiss();
      }
    });
    ll.addView(b2);

    Button b3 = new Button(this);
    b3.setText("GitHub");
    b3.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL)));
        dialog.dismiss();
      }
    });
    ll.addView(b3);

    Button b4 = new Button(this);
    b4.setText("Blog");
    b4.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(BLOG_URL)));
        dialog.dismiss();
      }
    });
    ll.addView(b4);

    dialog.setContentView(ll);
    dialog.show();
  }
}


 */