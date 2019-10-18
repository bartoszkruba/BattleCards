package inAndOutputAdapter

class ASCII {
    companion object {

        val ANSI_RESET = "\u001B[0m"
        val ANSI_BLUE = "\u001B[34m"
        val ANSI_RED = "\u001B[31m"
        val ANSI_PURPLE = "\u001B[35m"

        const val BATTLE_CARDS = """
 ______     ______     ______   ______   __         ______        ______     ______     ______     _____     ______    
/\  == \   /\  __ \   /\__  _\ /\__  _\ /\ \       /\  ___\      /\  ___\   /\  __ \   /\  == \   /\  __-.  /\  ___\   
\ \  __<   \ \  __ \  \/_/\ \/ \/_/\ \/ \ \ \____  \ \  __\      \ \ \____  \ \  __ \  \ \  __<   \ \ \/\ \ \ \___  \  
 \ \_____\  \ \_\ \_\    \ \_\    \ \_\  \ \_____\  \ \_____\     \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \____-  \/\_____\ 
  \/_____/   \/_/\/_/     \/_/     \/_/   \/_____/   \/_____/      \/_____/   \/_/\/_/   \/_/ /_/   \/____/   \/_____/ 

                                                                                                                       
"""
        const val CREATED_WITH = """
                                ___               _           _   __    __ _ _   _     
                               / __\ __ ___  __ _| |_ ___  __| | / / /\ \ (_) |_| |__  
                              / / | '__/ _ \/ _` | __/ _ \/ _` | \ \/  \/ / | __| '_ \ 
                             / /__| | |  __/ (_| | ||  __/ (_| |  \  /\  /| | |_| | | |
                             \____/_|  \___|\__,_|\__\___|\__,_|   \/  \/ |_|\__|_| |_|
                                                                                       
"""

        val KOTLIN = """
      WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWKOOO0NKOOO0XWWWWWWWWWWWWWWWWWWW
      WWW${ANSI_BLUE}NOkkkkkkkkk${ANSI_RED}OOOOOOOO0X${ANSI_RESET}NWWWWW0l;;:kWWWWWNOc;;;cONWWWWWWWWWWWWWWWNOddd0WWWNl   ;0l   .OWWWWWWWWWWWWWWWWWWW
      WWW${ANSI_BLUE}Xxllllll${ANSI_RED}oddddxxxdxOK${ANSI_RESET}NWWWWWWO.   oWWWW0c.  .:ONWWWWWWWWWWWWWWWWK,   cNWWNc   ,0x''':0WWWWWWWWWWWWWWWWWWW
      WWW${ANSI_BLUE}Xxllll${ANSI_RED}oodddddxdxOX${ANSI_RESET}NWWWWWWWWO.   oWWKl.   ;kNWWWNKkxdolodkKNWNOl.   'ddxKc   ,K0oooxXKxddkXXkoood0NWWWWW
      WWW${ANSI_BLUE}Xxl${ANSI_RED}ooodddddddxOX${ANSI_RESET}NWWWWWWWWWWO.   oXo.   ;kNWWWXx;.        .;x0;        .dc   ,Ko   .Od   .,.     .:0WWWW
      WWW${ANSI_BLUE}X${ANSI_RED}xoooddddddxOK${ANSI_RESET}NWWWWWWWWWWWWO.   ''   .lNWWWWK:   .';lol,.   ,l;.   .cco0c   ,Ko   .Od    .,cc'    :XWWW
      WWW${ANSI_RED}XxooooddddoxK${ANSI_RESET}WWWWWWWWWWWWWWO.         'xNWWXc   'kNNWWWNx.   cO,   cNWWNc   ,Ko   .Od   .oNWWK;   '0WWW
      WWW${ANSI_RED}Xxoooooooo${ANSI_BLUE}lldOX${ANSI_RESET}WWWWWWWWWWWWO.    ,o;   .cKW0'   lNWWWWWWNc   ,k,   cNWWNc   ,Ko   .Od   .kWWWNl   '0WWW
      WWW${ANSI_RED}Xxoooooo${ANSI_BLUE}lllllldOX${ANSI_RESET}WWWWWWWWWWO.   :XWNd.   'kK;   ,0WWWWWW0,   cO,   cNWWNc   ,Ko   .Od   .kWWWNl   '0WWW
      WWW${ANSI_RED}Xxlooo${ANSI_BLUE}lllllllllldOX${ANSI_RESET}WWWWWWWWO.   oWWWW0;   .cd'   .;cdxdc.   ,0K,   .lod0c   ,Ko   .Od   .kWWWNl   '0WWW
      WWW${ANSI_RED}Xxlo${ANSI_BLUE}lllllllllllllldOX${ANSI_RESET}WWWWWWO.   oWWWWWXo.   'ol'          'oKWNo.     .xc   ,Ko   .Od   .kWWWNl   '0WWW
      WWWNKO000000OOOOOOOOOOOKNWWWWWKdllo0WWWWWWWOolllxXXkollc:cldOXWWWWNOoccclkXOlllxX0ollxX0olldKWWWWOollxXWWW
      WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW"""
    }

}