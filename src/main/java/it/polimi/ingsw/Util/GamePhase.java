package it.polimi.ingsw.Util;

/**
 * GamePhase enum is used by the client to know which commands are available to the user at a specific time. <br>
 * <b>Available phases</b>: init_phase, nick_phase, select_match_phase, wait_phase, planification_phase, move_st_phase,
 *     move_mn_phase, move_cl_phase, play_ch_card_phase.
 */
public enum GamePhase {
    INIT_PHASE, NICK_PHASE, SELECT_MATCH_PHASE, WAIT_PHASE, PLANIFICATION_PHASE, MOVE_ST_PHASE,
    MOVE_MN_PHASE, MOVE_CL_PHASE, PLAY_CH_CARD_PHASE,
}
