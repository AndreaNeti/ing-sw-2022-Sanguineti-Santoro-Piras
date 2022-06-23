package it.polimi.ingsw.Util;

/**
 * GamePhase enum is used by the client to know which commands are available to the user at a specific time. <br>
 * <b>Available phases</b>: <br>
 * INIT_PHASE <br>
 * NICK_PHASE <br>
 * SELECT_MATCH_PHASE <br>
 * WAIT_PHASE <br>
 * PLANIFICATION_PHASE <br>
 * MOVE_ST_PHASE <br>
 * MOVE_MN_PHASE <br>
 * MOVE_CL_PHASE <br>
 * PLAY_CH_CARD_PHASE <br>
 */
public enum GamePhase {
    INIT_PHASE, NICK_PHASE, SELECT_MATCH_PHASE, WAIT_PHASE, PLANIFICATION_PHASE, MOVE_ST_PHASE,
    MOVE_MN_PHASE, MOVE_CL_PHASE, PLAY_CH_CARD_PHASE,
}
