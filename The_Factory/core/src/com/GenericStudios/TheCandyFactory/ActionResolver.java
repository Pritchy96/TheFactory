package com.GenericStudios.TheCandyFactory;

public interface ActionResolver {
  public void LoadInterstital();
  public void ShowInterstital();

    public boolean getSignedInGPGS();
    public void loginGPGS();
    public void submitScoreGPGS(int score);
    public void unlockAchievementGPGS(String achievementId);
    public void getLeaderboardGPGS();
    public void getAchievementsGPGS();

}
