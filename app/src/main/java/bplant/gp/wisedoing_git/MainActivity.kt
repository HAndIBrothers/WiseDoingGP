package bplant.gp.wisedoing_git

import android.app.LauncherActivity
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorSpace
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
/* [AdMob][TestKey:on] */
import com.google.android.gms.ads.MobileAds
/* TTS */
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.support.v7.app.AlertDialog
import android.widget.*
import org.w3c.dom.Text
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    /* Current GP 015 ER 001 */

    /* 2019-06-03 */
    /* [AdMob][TestKey:on] */
    private lateinit var mAdView : AdView

    /* 2019-06-05 */
    /* TTS */
    private lateinit var wiseTTS : TextToSpeech

    /* 2019-06-06 */
    private lateinit var timer : Timer
    /* Option */
    private var savTTSOption : Boolean = false
    /* Time */
    private var secTimer = 10 // [here] 지속적인 변경 시간(초)를 저장하기 위한 Int 변수 secTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* 2019-06-03 */// Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        /* [AdMob][TestKey:on] */
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713")
        /* [AdMob][TestKey:on] */
        mAdView = findViewById(R.id.adView)
        val adRequest =  AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        /* 2019-06-05 */
        /* TTS */
        wiseTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                // if there is no error then set language
                wiseTTS.language = Locale.KOREA
            }
        })

        // 여기서부터 코딩
        val txtWise = findViewById<TextView>(R.id.txtWise) // [activity_main] txtWise 텍스트 뷰를 가져오는 변수 txtWise
        val txtPerson = findViewById<TextView>(R.id.txtPerson) // [activity_main] txtPerson 텍스트 뷰를 가져오는 변수 txtPerson
        val imgUnder = findViewById<ImageView>(R.id.imgUnder) // [activity_main] imgUnder 이미지 뷰를 가져오는 변수 imgUnder
        /* Debug */
        val txtDebug = findViewById<TextView>(R.id.txtDebug) // [activity_main] txtDebug 텍스트 뷰를 가져오는 변수 txtDebug
        Log.d("wiseDGP", "[gp000][MainActivity][activity_main] 필요한 View 변수 저장")

        val clsWiseSize = 60 // [here] 명언의 갯수를 저장하는 Int 변수 clsWiseSize
        val clsWise = arrayOfNulls<ClassWise>(size = clsWiseSize) // [here] 명언을 저장하는 배열 Class 변수 clsWise
        clsWise[0] = ClassWise(thisWord = "살아있으면 뭐라도 해야 하는 거니까.", thisPerson = "(육룡이 나르샤) 분이", thisCategory = 0)
        clsWise[1] = ClassWise(thisWord = "처음에 부지런하지만 나중으로 갈수록 게을러지는 것은 인지상정입니다.\n 원컨대 전하께서는 나중을 삼가기를 항상 처음처럼 하십시오.", thisPerson = "한명회", thisCategory = 0)
        clsWise[2] = ClassWise(thisWord = "나는 생각한다 고로 나는 존재한다", thisPerson = "르네 데카르트", thisCategory = 0)
        clsWise[3] = ClassWise(thisWord = "변명 중에서 가장 어리석고 못난 것은 '시간이 없어서'라는 것이다.", thisPerson = "토머스 에디슨", thisCategory = 0)
        clsWise[4] = ClassWise(thisWord = "당신이 지금 달린다면 패배할 수도 있다.\n하지만 달리지 않으면 이미 당신은 진 것이다.", thisPerson = "버락 오바마", thisCategory = 0)
        clsWise[5] = ClassWise(thisWord = "바람이 불지 않으면 노를 저어라.", thisPerson = "윈스턴 처칠", thisCategory = 0)
        clsWise[6] = ClassWise(thisWord = "산을 움직이려 하는 자는 작은 돌을 들어내는 일로 시작한다.", thisPerson = "공자", thisCategory = 0)
        clsWise[7] = ClassWise(thisWord = "우주를 놀라게 하자!", thisPerson = "스티브 잡스", thisCategory = 0)
        clsWise[8] = ClassWise(thisWord = "가난하게 태어난 것은 당신의 실수가 아니지만, 죽을 때도 가난한 것은 당신의 실수이다.", thisPerson = "빌 게이츠", thisCategory = 0)
        clsWise[9] = ClassWise(thisWord = "뜨거운 열정보다 중요한 것은 지속적인 열정이다.", thisPerson = "마크 저커버그", thisCategory = 0)
        clsWise[10] = ClassWise(thisWord = "밀고 나갈 용기만 있다면 우리의 모든 꿈은 이루어진다.", thisPerson = "월트 디즈니", thisCategory = 0)
        clsWise[11] = ClassWise(thisWord = "하루종일 일하는 사람은 돈을 벌 시간이 없다.", thisPerson = "존 D.록펠러", thisCategory = 0)
        clsWise[12] = ClassWise(thisWord = "대가를 지불하지 않아도 되는 일은 아무런 가치가 없다.", thisPerson = "알베르트 아인슈타인", thisCategory = 0)
        clsWise[13] = ClassWise(thisWord = "행동과 힘든 연습이 없는 말은 아무런 가치가 없다.", thisPerson = "마이클 조던", thisCategory = 0)
        clsWise[14] = ClassWise(thisWord = "인간 세상에 실패라는 건 없다.", thisPerson = "사카모토 료마", thisCategory = 0)
        clsWise[15] = ClassWise(thisWord = "소심하게 굴기엔 인생이 너무 짧다.", thisPerson = "데일 카네기", thisCategory = 0)
        clsWise[16] = ClassWise(thisWord = "끝까지 하라.\n내가 살아가면서 가장 귀한 발견은 바로 인내였다.", thisPerson = "아이작 뉴턴", thisCategory = 0)
        clsWise[17] = ClassWise(thisWord = "나는 배울 것이 아무것도 없는 무지막지한 사람을 만난 적이 없다.", thisPerson = "갈릴레오 갈릴레이", thisCategory = 0)
        clsWise[18] = ClassWise(thisWord = "인간은 환경을 탓하면 패배자가 되고, 그것을 적극적으로 이용하면 승리자가 될 수 있다.", thisPerson = "오프라 윈프리", thisCategory = 0)
        clsWise[19] = ClassWise(thisWord = "삶은 과감한 모험이거나, 아니면 아무것도 아니다.", thisPerson = "헬렌 켈러", thisCategory = 0)
        clsWise[20] = ClassWise(thisWord = "안하고 죽어도 좋은 일만 내일로 미뤄라.", thisPerson = "파블로 피카소", thisCategory = 0)
        clsWise[21] = ClassWise(thisWord = "꿈은 도망치지 않는다.\n도망치는 것은 언제나 자신이다.", thisPerson = "(짱구는 못말려) 짱구아빠", thisCategory = 0)
        clsWise[22] = ClassWise(thisWord = "혼자 컸다고 자만하는 녀석은 클 자격이 없어.", thisPerson = "(짱구는 못말려) 짱구아빠", thisCategory = 1)
        clsWise[23] = ClassWise(thisWord = "나쁜 일을 했을 때는 잘못했다고 말하는 거야.", thisPerson = "(짱구는 못말려) 짱구", thisCategory = 1)
        clsWise[24] = ClassWise(thisWord = "한명의 여성을 계속 사랑하다니 멋진 일이잖아.", thisPerson = "(짱구는 못말려) 짱구아빠", thisCategory = 3)
        clsWise[25] = ClassWise(thisWord = "밝은 성격은 어떤 재산보다도 귀하다.", thisPerson = "앤드류 카네기", thisCategory = 1)
        clsWise[26] = ClassWise(thisWord = "늘 명심하라.\n성공하려는 너의 결심이 다른 어떤 것보다 중요하다.", thisPerson = "에이브러햄 링컨", thisCategory = 0)
        clsWise[27] = ClassWise(thisWord = "너는 왜 평범하게 노력하는가, 시시하게 살기를 원치 않으면서!", thisPerson = "존 F. 케네디", thisCategory = 0)
        clsWise[28] = ClassWise(thisWord = "질까보냐.", thisPerson = "혼다 소이치로", thisCategory = 0)
        clsWise[29] = ClassWise(thisWord = "생각이 가난한 사람들은 공통적인 한 행동 때문에 실패한다.\n그들의 인생은 기다리다가 끝이 난다.", thisPerson = "마윈", thisCategory = 0)
        clsWise[30] = ClassWise(thisWord = "위대한 행동이란 없다.\n위대한 사랑으로 행한 작은 행동들이 있을 뿐이다.", thisPerson = "테레사 수녀", thisCategory = 1)
        clsWise[31] = ClassWise(thisWord = "천사는 아름다운 꽃을 퍼뜨리는 사람이 아니라, 고뇌하는 사람을 위해 싸우는 사람일 것이다.", thisPerson = "플로렌스 나이팅게일", thisCategory = 1)
        clsWise[32] = ClassWise(thisWord = "당신이 포기할 때, 나는 시작한다.", thisPerson = "엘론 머스크", thisCategory = 0)
        clsWise[33] = ClassWise(thisWord = "내가 상대를 믿는 것과, 상대가 나를 배신하는 것은 아무런 관계도 없었던 거야.", thisPerson = "(십이국기) 나카지마 요코", thisCategory = 1)
        clsWise[34] = ClassWise(thisWord = "42", thisPerson = "(은하수를 여행하는 히치하이커를 위한 안내서)\n깊은 생각", thisCategory = 2)
        clsWise[35] = ClassWise(thisWord = "웃음이 없는 하루는 버린 하루다.", thisPerson = "찰리 채플린", thisCategory = 1)
        clsWise[36] = ClassWise(thisWord = "내 사전에 불가능은 없다.", thisPerson = "나폴레옹 보나파르트", thisCategory = 1)
        clsWise[37] = ClassWise(thisWord = "너는 머뭇거릴 수 있지만, 시간은 그렇지 않다.", thisPerson = "벤자민 프랭클린", thisCategory = 0)
        clsWise[38] = ClassWise(thisWord = "오늘 나무 그늘에서 쉴 수 있는 이유는, 예전에 나무를 심었기 때문이다.", thisPerson = "워렌 버핏", thisCategory = 0)
        clsWise[39] = ClassWise(thisWord = "네가 누구인지, 무엇인지 말해줄 사람은 필요 없다.\n너는 그냥 너 자신일 뿐이다.", thisPerson = "존 레논", thisCategory = 2)
        clsWise[40] = ClassWise(thisWord = "인생에는 수많은 모순이 있지만 그것을 해결할 길은 사랑 뿐이다.", thisPerson = "레프 톨스토이", thisCategory = 3)
        clsWise[41] = ClassWise(thisWord = "악담을 듣거나 혼난다면 기뻐하라.\n칭찬을 하면 마음을 가다듬어라.", thisPerson = "레프 톨스토이", thisCategory = 1)
        clsWise[42] = ClassWise(thisWord = "우리는 가난을 칭송하지 않는다.\n다만 가난에 굴하지 않는 사람을 칭송할 뿐이다.", thisPerson = "레프 톨스토이", thisCategory = 0)
        clsWise[43] = ClassWise(thisWord = "가장 유능한 자는 가장 배우려 하는 자이다.", thisPerson = "요한 볼프강 폰 괴테", thisCategory = 0)
        clsWise[44] = ClassWise(thisWord = "오늘 가장 좋게 웃는 자는 분명 최후에도 웃을 것이다.", thisPerson = "프리드리히 니체", thisCategory = 1)
        clsWise[45] = ClassWise(thisWord = "사랑은 두 사람이 마주 쳐다보는 것이 아니라 함께 같은 방향을 바라보는 것이다.", thisPerson = "앙투안 드 생텍쥐페리", thisCategory = 3)
        clsWise[46] = ClassWise(thisWord = "행복은 생각, 말 행동이 조화를 이룰 때 찾아온다.", thisPerson = "마하트마 간디", thisCategory = 1)
        clsWise[47] = ClassWise(thisWord = "당신이 취하는 모든 행동이 보잘 것 없다 하더라도, 중요한 것은 일단 행동을 취하는 것이다.", thisPerson = "마하트마 간디", thisCategory = 0)
        clsWise[48] = ClassWise(thisWord = "날지 못한다면 뛰십시오, 뛰지 못한다면 걸으십시오, 걷지 못한다면 기십시오.\n무엇을 하던 가장 중요한 것은, 앞으로 나아가야 한다는 것입니다.", thisPerson = "마틴 루터 킹", thisCategory = 0)
        clsWise[49] = ClassWise(thisWord = "용서는 가끔 발생하는 행위가 아니라, 지속적으로 우리가 지녀야할 태도입니다.", thisPerson = "마틴 루터 킹", thisCategory = 1)
        clsWise[50] = ClassWise(thisWord = "스스로 운이 나쁘다고 생각하지 않는 한은 나쁜 운이란 없다.", thisPerson = "정주영", thisCategory = 1)
        clsWise[51] = ClassWise(thisWord = "이봐, 해보기나 했어?", thisPerson = "정주영", thisCategory = 0)
        clsWise[52] = ClassWise(thisWord = "우리 모두는 별이고, 반짝일 권리가 있다.", thisPerson = "마릴린 먼로", thisCategory = 2)
        clsWise[53] = ClassWise(thisWord = "이것 역시 곧 지나가리라.", thisPerson = "솔로몬", thisCategory = 2)
        clsWise[54] = ClassWise(thisWord = "부처의 경지에 도달한 사람도 인과응보의 세상 이치에서 자유로울 수 없다.", thisPerson = "석가모니", thisCategory = 1)
        clsWise[55] = ClassWise(thisWord = "사랑은 무엇보다도 자신을 위한 선물이다.", thisPerson = "장 아누이", thisCategory = 3)
        clsWise[56] = ClassWise(thisWord = "사랑이 지나치는 법은 없다.", thisPerson = "빅토르 위고", thisCategory = 3)
        clsWise[57] = ClassWise(thisWord = "궁핍은 영혼과 정신을 낳고, 불행은 위대한 인물을 낳는다.", thisPerson = "빅토르 위고", thisCategory = 0)
        clsWise[58] = ClassWise(thisWord = "모두에게 넌 과분하지.", thisPerson = "(디즈니 : 미녀와 야수 2017) 개스톤", thisCategory = 2)
        clsWise[59] = ClassWise(thisWord = "아무리 기적이라 해도 시간이 좀 걸린단다.", thisPerson = "(디즈니 : 신데렐라 1950) 요정대모", thisCategory = 1)
        Log.d("wiseDGP", "[gp001][MainActivity][here] 명언 초기화")

        fun fncChangeWise(tmpClass : Array<ClassWise?>, tmpSize : Int) {
            val thisRandom = Random().nextInt(tmpSize) // [here:fncChangeWise] 명언 번호를 무작위로 뽑기 위한 Int 변수 thisRandom
            val txtWiseText = tmpClass[thisRandom]?.word
            txtWise.text = txtWiseText
            val txtPersonText = "by "+ tmpClass[thisRandom]?.person
            txtPerson.text = txtPersonText

            Log.d("wiseDGP", "[gp002][MainActivity][here:fncChangeWise] 명언 변환")
        }
        fncChangeWise(clsWise, clsWiseSize)
        Log.d("wiseDGP", "[gp003][MainActivity][here] 초기화면 명언 출력")

        /* 2019-06-06 */
        /* Repeat Play */
        timer = Timer("SettingUp", false) // [here] 명언을 지정 시간(초)마다 부르기 위한 Timer 변수 timer
        var secCurrent = 0 // [here] 현재 몇초를 지나고 있는지 저장하기 위한 Int 변수 secCurrent
        Log.d("wiseDGP", "[gp004][MainActivity][here] 명언 시간 반복 : $secTimer 초 지정")

        /* 2019-06-04 test 카테고리에 맞춰서 변경해줄 변수 */
        var chkWise : Array<ClassWise?> = clsWise
        var chkWiseSize : Int = clsWiseSize
        
        timer.scheduleAtFixedRate(delay = 1000, period = 1000) {
            secCurrent++
            if (secCurrent >= secTimer) {
                secCurrent = 0
                fncChangeWise(chkWise, chkWiseSize)

                /* TTS */
                if (wiseTTS.isSpeaking){
                    wiseTTS.stop()
                    wiseSpeaker(txtWise.text.toString())
                } else {
                    wiseSpeaker(txtWise.text.toString())
                }

                /* Debug */
                txtDebug.text = secCurrent.toString()
                Log.d("wiseDGP", "[gp007][MainActivity][here:timer.scheduleAtFixedRate] secCurrent >= secTimer")
            } else {
                /* Debug */
                txtDebug.text = secCurrent.toString()
                Log.d("wiseDGP", "[gp008][MainActivity][here:timer.scheduleAtFixedRate] secCurrent < secTimer")
            }

            /* Change Loading Image */
            if (secCurrent == 0) {
                imgUnder.setImageResource(R.drawable.bnr_proto1_0)
                Log.d("wiseDGP", "[gp009][MainActivity][here:timer.scheduleAtFixedRate] 명언이 바뀌었을 때(secCurrent 0일때) imgUnder 초기화")
            } else {
                val thisChecker = secCurrent.toFloat() / secTimer.toFloat()

                when {
                    thisChecker >= 0.8 -> imgUnder.setImageResource(R.drawable.bnr_proto1_4)
                    thisChecker >= 0.6 -> imgUnder.setImageResource(R.drawable.bnr_proto1_3)
                    thisChecker >= 0.4 -> imgUnder.setImageResource(R.drawable.bnr_proto1_2)
                    thisChecker >= 0.2 -> imgUnder.setImageResource(R.drawable.bnr_proto1_1)
                    /* Debug */
                    else -> Log.d("wiseDGP", "[er000][MainActivity][here:timer.scheduleAtFixedRate] $secCurrent / $secTimer : $thisChecker")
                }
                Log.d("wiseDGP", "[gp010][MainActivity][here:timer.scheduleAtFixedRate] 현재 시간의 진행상황(secCurrent / secTimer)에 따라서 imgUnder 의 그림 변경")
            }

            Log.d("wiseDGP", "[gp005][MainActivity][here:timer.scheduleAtFixedRate] 지정 시간 반복 실행")
        }

        /* 2019-06-06 */
        /* Timer 초기화 */
        fun iniTimer() {
            secCurrent = 0
            fncChangeWise(chkWise, chkWiseSize)
            imgUnder.setImageResource(R.drawable.bnr_proto1_0)
        }

        /* Category */
        val btnWiseAll = findViewById<Button>(R.id.btnWiseAll)
        val btnWiseGrowth = findViewById<Button>(R.id.btnWiseGrowth)
        val btnWiseAttitude = findViewById<Button>(R.id.btnWiseAttitude)
        val btnWiseHealing = findViewById<Button>(R.id.btnWiseHealing)
        val btnWiseLove = findViewById<Button>(R.id.btnWiseLove)

        val forTextWiseAll = "전체\n"
        val forTextWiseGrowth = "발전\n"
        val forTextWiseAttitude = "태도\n"
        val forTextWiseHealing = "힐링\n"
        val forTextWiseLove = "사랑\n"

        /* Category Load : 전체 */
        val btnWiseAllText = "$forTextWiseAll ($clsWiseSize)"
        btnWiseAll.text = btnWiseAllText

        /* Category Load : 그외 */
        val clsCategorySize = intArrayOf(0, 0, 0, 0)
        for (i in 0..clsWiseSize) {
            if(i < clsWiseSize){
                when {
                    clsWise[i]?.category == 0 -> clsCategorySize[0]++
                    clsWise[i]?.category == 1 -> clsCategorySize[1]++
                    clsWise[i]?.category == 2 -> clsCategorySize[2]++
                    clsWise[i]?.category == 3 -> clsCategorySize[3]++
                }
            } else {
            }
        }
        val btnWiseGrowthText = forTextWiseGrowth + "(" + clsCategorySize[0] + ")"
        btnWiseGrowth.text = btnWiseGrowthText
        val btnWiseAttitudeText = forTextWiseAttitude+ "(" + clsCategorySize[1] + ")"
        btnWiseAttitude.text = btnWiseAttitudeText
        val btnWiseHealingText = forTextWiseHealing + "(" + clsCategorySize[2] + ")"
        btnWiseHealing.text = btnWiseHealingText
        val btnWiseLoveText = forTextWiseLove + "(" + clsCategorySize[3] + ")"
        btnWiseLove.text = btnWiseLoveText

        /* Category Array */
        val clsCategoryGrowth = arrayOfNulls<ClassWise>(clsCategorySize[0])
        val clsCategoryAttitude = arrayOfNulls<ClassWise>(clsCategorySize[1])
        val clsCategoryHealing = arrayOfNulls<ClassWise>(clsCategorySize[2])
        val clsCategoryLove = arrayOfNulls<ClassWise>(clsCategorySize[3])
        val tmpNumber = intArrayOf(0, 0, 0, 0)

        for (i in 0..clsWiseSize) {
            if (i < clsWiseSize) {
                when {
                    clsWise[i]?.category == 0 -> {
                        clsCategoryGrowth[tmpNumber[0]] = clsWise[i]
                        tmpNumber[0]++
                    }
                    clsWise[i]?.category == 1 -> {
                        clsCategoryAttitude[tmpNumber[1]] = clsWise[i]
                        tmpNumber[1]++
                    }
                    clsWise[i]?.category == 2 -> {
                        clsCategoryHealing[tmpNumber[2]] = clsWise[i]
                        tmpNumber[2]++
                    }
                    clsWise[i]?.category == 3 -> {
                        clsCategoryLove[tmpNumber[3]] = clsWise[i]
                        tmpNumber[3]++
                    }
                    else -> Log.d("wiseDGP", "[er001][MainActivity][here:Category]")
                }
            } else {
            }
        }

        /* 2019-06-04 */
        /* Category Color Change */
        fun fncViewChanger(tmpView : Button) {
            btnWiseAll.setBackgroundColor(Color.rgb(229, 229, 229))
            btnWiseAll.setTextColor(Color.rgb(50, 50, 50))
            btnWiseGrowth.setBackgroundColor(Color.rgb(229, 229, 255))
            btnWiseGrowth.setTextColor(Color.rgb(50, 50, 50))
            btnWiseAttitude.setBackgroundColor(Color.rgb(255, 255, 229))
            btnWiseAttitude.setTextColor(Color.rgb(50, 50, 50))
            btnWiseHealing.setBackgroundColor(Color.rgb(229, 242, 229))
            btnWiseHealing.setTextColor(Color.rgb(50, 50, 50))
            btnWiseLove.setBackgroundColor(Color.rgb(255, 229, 229))
            btnWiseLove.setTextColor(Color.rgb(50, 50, 50))

            when(tmpView) {
                btnWiseAll -> {
                    btnWiseAll.setBackgroundColor(Color.rgb(50, 50, 50))
                    btnWiseAll.setTextColor(Color.rgb(255, 255, 255))
                }
                btnWiseGrowth -> {
                    btnWiseGrowth.setBackgroundColor(Color.rgb(50, 50, 255))
                    btnWiseGrowth.setTextColor(Color.rgb(255, 255, 255))
                }
                btnWiseAttitude -> btnWiseAttitude.setBackgroundColor(Color.rgb(255, 255, 50))
                btnWiseHealing -> btnWiseHealing.setBackgroundColor(Color.rgb(50, 153, 50))
                btnWiseLove -> btnWiseLove.setBackgroundColor(Color.rgb(255, 50, 50))
            }
        }

        /* 2019-06-04 */
        /* Save & Load : Category */
        val prfCategory = this.getPreferences(0)
        val savCategory = prfCategory.edit()
        val savCategoryNumber = prfCategory.getInt("currentCategory", 999)

        /* 2019-06-04 */
        /* Category Click */
        btnWiseAll.setOnClickListener{
                chkWise = clsWise
                chkWiseSize = clsWiseSize
                fncChangeWise(chkWise, chkWiseSize)

                fncViewChanger(btnWiseAll)
                savCategory.putInt("currentCategory", 999).apply()

                Log.d("wiseDGP", "[gp011][MainActivity][here:Category Click] btnWiseAll 클릭")
        }
        btnWiseGrowth.setOnClickListener{
                chkWise = clsCategoryGrowth
                chkWiseSize = clsCategorySize[0]
                fncChangeWise(chkWise, chkWiseSize)

                fncViewChanger(btnWiseGrowth)
                savCategory.putInt("currentCategory", 0).apply()

                Log.d("wiseDGP", "[gp012][MainActivity][here:Category Click] btnWiseGrowth 클릭")
        }
        btnWiseAttitude.setOnClickListener{
                chkWise = clsCategoryAttitude
                chkWiseSize = clsCategorySize[1]
                fncChangeWise(chkWise, chkWiseSize)

                fncViewChanger(btnWiseAttitude)
                savCategory.putInt("currentCategory", 1).apply()

                Log.d("wiseDGP", "[gp013][MainActivity][here:Category Click] btnWiseAttitude 클릭")
        }
        btnWiseHealing.setOnClickListener{
                chkWise = clsCategoryHealing
                chkWiseSize = clsCategorySize[2]
                fncChangeWise(chkWise, chkWiseSize)

                fncViewChanger(btnWiseHealing)
                savCategory.putInt("currentCategory", 2).apply()

                Log.d("wiseDGP", "[gp014][MainActivity][here:Category Click] btnWiseHealing 클릭")
        }
        btnWiseLove.setOnClickListener {
                chkWise = clsCategoryLove
                chkWiseSize = clsCategorySize[3]
                fncChangeWise(chkWise, chkWiseSize)

                fncViewChanger(btnWiseLove)
                savCategory.putInt("currentCategory", 3).apply()

                Log.d("wiseDGP", "[gp015][MainActivity][here:Category Click] btnWiseLove 클릭")
        }

        /* 2019-06-06 */
        /* Option */

        /* Option Load */
        savTTSOption = prfCategory.getBoolean("currentTTSOption", false)
        secTimer = prfCategory.getInt("currentTimeOption", 10)
        Toast.makeText(this, secTimer.toString(), Toast.LENGTH_SHORT).show()

        val btnOption = findViewById<Button>(R.id.btnOption)
        btnOption.setOnClickListener{
            val alertOption = AlertDialog.Builder(this@MainActivity)
            alertOption.setTitle("옵션")

            val alertOptionItem = arrayOf("명언 전환시간", "TTS")
            alertOption.setSingleChoiceItems(alertOptionItem, -1) { _ , i ->
                when(i) {
                    0 -> {
                        val dialogOptionTime = AlertDialog.Builder(this@MainActivity)
                        dialogOptionTime.setTitle("명언 전환시간 설정")

                        val alertOptionTime = arrayOf("10초", "1분", "10분")
                        dialogOptionTime.setSingleChoiceItems(alertOptionTime, -1) { dialogTime, k ->
                            when(k) {
                                0 -> {
                                    secTimer = 10
                                    savCategory.putInt("currentTimeOption", secTimer).apply()
                                }
                                1 -> {
                                    secTimer = 60
                                    savCategory.putInt("currentTimeOption", secTimer).apply()
                                }
                                2 -> {
                                    secTimer = 600
                                    savCategory.putInt("currentTimeOption", secTimer).apply()
                                }
                            }
                            iniTimer()
                            dialogTime.dismiss()
                        }

                        dialogOptionTime.setNeutralButton("닫기") {dialog : DialogInterface, _ : Int -> dialog.cancel() }

                        val alertDialogTime = dialogOptionTime.create()
                        alertDialogTime.show()
                    }

                    1 -> {
                        val dialogOptionTTS = AlertDialog.Builder(this@MainActivity)
                        dialogOptionTTS.setTitle("TTS 설정")

                        val alertOptionTTS = arrayOf("ON", "OFF")
                        dialogOptionTTS.setSingleChoiceItems(alertOptionTTS, -1) { dialogTTS, j ->
                            // Toast.makeText(this, alertOptionTTS[i], Toast.LENGTH_SHORT).show()
                            when(j) {
                                0 -> {
                                    savTTSOption = true
                                    savCategory.putBoolean("currentTTSOption", savTTSOption).apply()
                                }
                                1 -> {
                                    savTTSOption = false
                                    savCategory.putBoolean("currentTTSOption", savTTSOption).apply()
                                }
                            }
                            dialogTTS.dismiss()
                        }

                        dialogOptionTTS.setNeutralButton("닫기") { dialog : DialogInterface, _ : Int -> dialog.cancel() }

                        val alertDialogTTS = dialogOptionTTS.create()
                        alertDialogTTS.show()
                    }
                }
            }

            alertOption.setNeutralButton("닫기") { dialog : DialogInterface, _ : Int ->
                dialog.cancel()
            }

            val alertDialog = alertOption.create()
            alertDialog.show()
        }

        /* 2019-06-04 */
        /* Save & Load Category */
        Log.d("wiseDGP", "[gp016][MainActivity][here:Category Click] 'currentCategory' $savCategoryNumber 로드")
        when(savCategoryNumber) {
            999 -> btnWiseAll.performClick()
            0 -> btnWiseGrowth.performClick()
            1 -> btnWiseAttitude.performClick()
            2 -> btnWiseHealing.performClick()
            3 -> btnWiseLove.performClick()
        }

        /* Debug */
        /*
        btnChangeWise.setOnClickListener(
            View.OnClickListener {
                fncChangeWise(clsWise, clsWiseSize)
                Log.d("wiseDGP", "[gp006][MainActivity][here:btnChangeWise.setOnClickListener] btnChangeWise 클릭")
            }
        )
        */
    }

    /* 2019-06-05 */
    /* TTS */
    private fun wiseSpeaker(toSpeak : String) {
        when (savTTSOption) {
            true -> {
                if (toSpeak == "") {
                    // if there is no text
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                } else {
                    // if there is text
                    // Toast.makeText(this, toSpeak, Toast.LENGTH_SHORT).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        wiseTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
                    } else {
                        wiseTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
                    }
                }
            }
        }
    }

    override fun onPause() {
        timer.cancel()
        if (wiseTTS.isSpeaking) {
            // if speaking then stop
            wiseTTS.stop()
        }
        super.onPause()
    }
}
