/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evm;

/**
 *
 * @author User
 */
public class VoteHandler {
    
    public void voteNominee(String username, int n){
        DatabaseHandler db = new DatabaseHandler();
        db.incrementVote(username, n);
    }
    
}
