package bplant.gp.wisedoing_git

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
/* [AdMob][TestKey:on] */
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

    /* Current GP 010 ER 000 */

    /* 2019-06-03 */
    /* [AdMob][TestKey:on] */
    lateinit var mAdView : AdView

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

        // 여기서부터 코딩
        val txtWise = findViewById<TextView>(R.id.txtWise) // [activity_main] txtWise 텍스트 뷰를 가져오는 변수 txtWise
        val txtPerson = findViewById<TextView>(R.id.txtPerson) // [activity_main] txtPerson 텍스트 뷰를 가져오는 변수 txtPerson
        val imgUnder = findViewById<ImageView>(R.id.imgUnder) // [activity_main] imgUnder 이미지 뷰를 가져오는 변수 imgUnder
        /* Debug */
        val txtDebug = findViewById<TextView>(R.id.txtDebug) // [activity_main] txtDebug 텍스트 뷰를 가져오는 변수 txtDebug
        Log.d("wiseDGP", "[gp000][MainActivity][activity_main] 필요한 View 변수 저장")

        val clsWiseSize = 35 // [here] 명언의 갯수를 저장하는 Int 변수 clsWiseSize
        var clsWise = arrayOfNulls<ClassWise>(size = clsWiseSize) // [here] 명언을 저장하는 배열 Class 변수 clsWise
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
        clsWise[23] = ClassWise(thisWord = "나쁜 일을 했을 때는 잘못했다고 말하는 거야.", thisPerson = "(짱구는 못말려) 짱구", thisCategory = 2)
        clsWise[24] = ClassWise(thisWord = "한명의 여성을 계속 사랑하다니 멋진 일이잖아.", thisPerson = "(짱구는 못말려) 짱구아빠", thisCategory = 3)
        clsWise[25] = ClassWise(thisWord = "밝은 성격은 어떤 재산보다도 귀하다.", thisPerson = "앤드류 카네기", thisCategory = 1)
        clsWise[26] = ClassWise(thisWord = "늘 명심하라.\n성공하려는 너의 결심이 다른 어떤 것보다 중요하다.", thisPerson = "에이브러햄 링컨", thisCategory = 0)
        clsWise[27] = ClassWise(thisWord = "너는 왜 평범하게 노력하는가, 시시하게 살기를 원치 않으면서!", thisPerson = "존 F. 케네디", thisCategory = 0)
        clsWise[28] = ClassWise(thisWord = "질까보냐.", thisPerson = "혼다 소이치로", thisCategory = 0)
        clsWise[29] = ClassWise(thisWord = "생각이 가난한 사람들은 공통적인 한 행동 때문에 실패한다.\n그들의 인생은 기다리다가 끝이 난다.", thisPerson = "마윈", thisCategory = 0)
        clsWise[30] = ClassWise(thisWord = "위대한 행동이란 없다.\n위대한 사랑으로 행한 작은 행동들이 있을 뿐이다.", thisPerson = "테레사 수녀", thisCategory = 1)
        clsWise[31] = ClassWise(thisWord = "천사는 아름다운 꽃을 퍼뜨리는 사람이 아니라, 고뇌하는 사람을 위해 싸우는 사람일 것이다.", thisPerson = "플로렌스 나이팅게일", thisCategory = 1)
        clsWise[32] = ClassWise(thisWord = "당신이 포기할 때, 나는 시작한다.", thisPerson = "엘론 머스크", thisCategory = 0)
        clsWise[33] = ClassWise(thisWord = "내가 상대를 믿는 것과, 상대가 나를 배신하는 것은 아무런 관계도 없었던 거야.", thisPerson = "마윈", thisCategory = 1)
        clsWise[34] = ClassWise(thisWord = "42", thisPerson = "(은하수를 여행하는 히치하이커를 위한 안내서) 깊은 생각", thisCategory = 2)
        Log.d("wiseDGP", "[gp001][MainActivity][here] 명언 초기화")

        fun fncChangeWise() {
            var thisRandom = Random().nextInt(clsWiseSize) // [here:fncChangeWise] 명언 번호를 무작위로 뽑기 위한 Int 변수 thisRandom
            txtWise.text = clsWise[thisRandom]?.word
            txtPerson.text = clsWise[thisRandom]?.person
            Log.d("wiseDGP", "[gp002][MainActivity][here:fncChangeWise] 명언 변환")
        }
        fncChangeWise()
        Log.d("wiseDGP", "[gp003][MainActivity][here] 초기화면 명언 출력")

        val timer = Timer("SettingUp", false) // [here] 명언을 지정 시간(초)마다 부르기 위한 Timer 변수 timer
        var secTimer = 5 // [here] 지속적인 변경 시간(초)를 저장하기 위한 Int 변수 secTimer
        var secCurrent = 0 // [here] 현재 몇초를 지나고 있는지 저장하기 위한 Int 변수 secCurrent
        Log.d("wiseDGP", "[gp004][MainActivity][here] 명언 시간 반복 : $secTimer 초 지정")
        timer.scheduleAtFixedRate(delay = 1000, period = 1000) {
            secCurrent++
            if (secCurrent >= secTimer) {
                secCurrent = 0
                fncChangeWise()
                /* Debug */
                txtDebug.text = secCurrent.toString()
                Log.d("wiseDGP", "[gp007][MainActivity][here:timer.scheduleAtFixedRate] secCurrent >= secTimer")
            } else {
                /* Debug */
                txtDebug.text = secCurrent.toString()
                Log.d("wiseDGP", "[gp008][MainActivity][here:timer.scheduleAtFixedRate] secCurrent < secTimer")
            }

            /*2019-06-02*/

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

        btnChangeWise.setOnClickListener(
            View.OnClickListener {
                fncChangeWise()
                Log.d("wiseDGP", "[gp006][MainActivity][here:btnChangeWise.setOnClickListener] btnChangeWise 클릭")
            }
        )

    }
}
