package bplant.gp.wisedoing_git

import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
/* [AdMob][TestKey:on] */
import com.google.android.gms.ads.MobileAds
/* TTS */
import android.speech.tts.TextToSpeech
import android.support.v4.os.ConfigurationCompat
import android.support.v7.app.AlertDialog
import android.widget.*

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

    /* 2019-06-09 */
    private var chkLanguage : Int = 99

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

        /* 2019-06-04 */
        /* Save & Load : Category */
        val prfCategory = this.getPreferences(0)
        val savCategory = prfCategory.edit()
        val savCategoryNumber = prfCategory.getInt("currentCategory", 999)

        /* 2019-06-09 */
        chkLanguage = prfCategory.getInt("currentLanguageOption", 99)

        if (chkLanguage == 99){
            val thisTempLocale = ConfigurationCompat.getLocales(resources.configuration)[0].toString()
            Toast.makeText(this, thisTempLocale, Toast.LENGTH_LONG).show()

            chkLanguage = when(ConfigurationCompat.getLocales(resources.configuration)[0].toString()) {
                "ko_KR" -> 0
                "ja_JP" -> 1
                "en_US" -> 2
                else -> 2
            }

            savCategory.putInt("currentLanguageOption", chkLanguage).apply()
        }

        /* 2019-06-05 */
        /* TTS */
        wiseTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                // if there is no error then set language
                wiseTTSLanguage(wiseTTS)
            }
        })

        // 여기서부터 코딩
        val txtWise = findViewById<TextView>(R.id.txtWise) // [activity_main] txtWise 텍스트 뷰를 가져오는 변수 txtWise
        val txtPerson = findViewById<TextView>(R.id.txtPerson) // [activity_main] txtPerson 텍스트 뷰를 가져오는 변수 txtPerson
        val imgUnder = findViewById<ImageView>(R.id.imgUnder) // [activity_main] imgUnder 이미지 뷰를 가져오는 변수 imgUnder
        /* Debug */
        val txtDebug = findViewById<TextView>(R.id.txtDebug) // [activity_main] txtDebug 텍스트 뷰를 가져오는 변수 txtDebug
        Log.d("wiseDGP", "[gp000][MainActivity][activity_main] 필요한 View 변수 저장")

        val clsWiseSize = 70 // [here] 명언의 갯수를 저장하는 Int 변수 clsWiseSize
        val clsWise = arrayOfNulls<ClassWise>(size = clsWiseSize) // [here] 명언을 저장하는 배열 Class 변수 clsWise
        clsWise[	0	] = ClassWise( thisWord = "	살아있으면 뭐라도 해야 하는 거니까.	", thisPerson = "	(육룡이 나르샤) 분이	", thisCategory = 	0	, thisWordJ = "	生きていれば、何かをやらないと。	", thisPersonJ = "	『六龍が飛ぶ』プ二	", thisWordE = "	If you are living, you should do something.	", thisPersonE = "	(Six Flying Dragons) Boon-yi	")
        clsWise[	1	] = ClassWise( thisWord = "	처음에 부지런하지만 나중으로 갈수록 게을러지는 것은 인지상정입니다. 원컨대 전하께서는 나중을 삼가기를 항상 처음처럼 하십시오.	", thisPerson = "	한명회	", thisCategory = 	0	, thisWordJ = "	始めは勤勉ですが後になると怠惰になるのが人の常です。 是非殿下は後を謹んで、常に始めのようにしてください。	", thisPersonJ = "	韓明カイ	", thisWordE = "	At first, anybody is diligence, but they're always become lazy later. Your Highness, Please forget the Last, and do not forget the Start.	", thisPersonE = "	Han Myeonghoe	")
        clsWise[	2	] = ClassWise( thisWord = "	나는 생각한다 고로 나는 존재한다.	", thisPerson = "	르네 데카르트	", thisCategory = 	0	, thisWordJ = "	我思う、ゆえに我あり	", thisPersonJ = "	ルネ・デカルト	", thisWordE = "	I think, therefore I am.	", thisPersonE = "	René Descartes	")
        clsWise[	3	] = ClassWise( thisWord = "	가장 확실한 성공법은 한 번 더 시도하는 것이다.	", thisPerson = "	토머스 에디슨	", thisCategory = 	0	, thisWordJ = "	成功する上で最も確実な方法は常にただもう一度トライすることである。	", thisPersonJ = "	トーマス・アルバ・エジソン	", thisWordE = "	The most certain way to succeed is always to try just one more time.	", thisPersonE = "	Thomas Alva Edison	")
        clsWise[	4	] = ClassWise( thisWord = "	당신이 지금 달린다면 질 가능성도 있다. 하지만 달리지 않으면 이미 당신은 진 것이다.	", thisPerson = "	버락 오바마	", thisCategory = 	0	, thisWordJ = "	あなたが今走ると負ける可能性もある。 だが走らないと貴方は既に負けている。	", thisPersonJ = "	バラク・オバマ	", thisWordE = "	If you run you stand a chance of losing, but if you don’t run you’ve already lost.	", thisPersonE = "	Barack Obama	")
        clsWise[	5	] = ClassWise( thisWord = "	만약 지옥을 지나가고 있다면, 멈추지 말고 가라.	", thisPerson = "	윈스턴 처칠	", thisCategory = 	1	, thisWordJ = "	もしも地獄の真っ只中にいるのなら、そのまま突き進むがいい。	", thisPersonJ = "	ウィンストン・チャーチル	", thisWordE = "	If you are going through hell, keep going.	", thisPersonE = "	Sir Winston Churchill	")
        clsWise[	6	] = ClassWise( thisWord = "	산을 움직이려 하는 자는 작은 돌을 들어내는 일로 시작한다.	", thisPerson = "	공자	", thisCategory = 	0	, thisWordJ = "	山を移さば小石から	", thisPersonJ = "	孔子	", thisWordE = "	The man who moves a mountain begins by carrying away small stones.	", thisPersonE = "	Confucius	")
        clsWise[	7	] = ClassWise( thisWord = "	우리는 우주를 놀라게 하기 위해 여기에 있다.	", thisPerson = "	스티브 잡스	", thisCategory = 	0	, thisWordJ = "	我々は宇宙を驚かせる為にここにいる。	", thisPersonJ = "	スティーブ・ジョブズ	", thisWordE = "	We're here to put a dent in the universe.	", thisPersonE = "	Steve Jobs	")
        clsWise[	8	] = ClassWise( thisWord = "	가난하게 태어난 것은 당신의 실수가 아니지만, 죽을 때도 가난한 것은 당신의 실수이다.	", thisPerson = "	빌 게이츠	", thisCategory = 	0	, thisWordJ = "	生まれた時に貧乏なのはあなたのせいではない。しかし、死ぬ時に貧乏なのはお前のミスだ。	", thisPersonJ = "	ビル・ゲイツ	", thisWordE = "	If you are born Poor its not your mistake, but if you die poor its your mistake.	", thisPersonE = "	Bill Gates	")
        clsWise[	9	] = ClassWise( thisWord = "	사람들은 네가 무슨 말을 하는지 신경쓰지 않는다, 그들은 네가 뭘 만들었는지를 신경 쓴다.	", thisPerson = "	마크 저커버그	", thisCategory = 	0	, thisWordJ = "	人々はあなたが何を言っているのか気にしない、彼らはあなたが作ったものを気にする。	", thisPersonJ = "	マーク・ザッカーバーグ	", thisWordE = "	People don’t care about what you say, they care about what you build.	", thisPersonE = "	Mark Zuckerberg	")
        clsWise[	10	] = ClassWise( thisWord = "	밀고 나갈 용기만 있다면, 우리의 모든 꿈은 이루어진다.	", thisPerson = "	월트 디즈니	", thisCategory = 	0	, thisWordJ = "	追い求める勇気があれば、すべての夢はかなう.	", thisPersonJ = "	ウォルト・ディズニー	", thisWordE = "	All our dreams can come true, if we have the courage to pursue them.	", thisPersonE = "	Walt Disney	")
        clsWise[	11	] = ClassWise( thisWord = "	네가 볼 수 있는만큼 멀리 가라; 네가 거기에 도착한다면, 너는 더 먼곳을 볼 수 있게 될 것이다.	", thisPerson = "	J. P. 모건	", thisCategory = 	0	, thisWordJ = "	限界まで行け、そこに辿り着けば、もっと先が見えるようになる。	", thisPersonJ = "	ジョン・モルガン	", thisWordE = "	Go as far as you can see; when you get there, you'll be able to see farther.	", thisPersonE = "	J. P. Morgan	")
        clsWise[	12	] = ClassWise( thisWord = "	6살짜리 아이에게 설명하지 못한다면, 너는 그것을 이해하지 못한 것이다.	", thisPerson = "	알베르트 아인슈타인	", thisCategory = 	1	, thisWordJ = "	6歳の子供に説明できなければ、理解したとは言えない。	", thisPersonJ = "	アルベルト・アインシュタイン	", thisWordE = "	If you can't explain it to a six year old, you don't understand it yourself.	", thisPersonE = "	Albert Einstein	")
        clsWise[	13	] = ClassWise( thisWord = "	한 번 그만두면 그건 습관이 된다. 절대 그만두지 마라!	", thisPerson = "	마이클 조던	", thisCategory = 	0	, thisWordJ = "	一度でもあきらめてしまうと、それが癖になる。絶対にあきらめるな！	", thisPersonJ = "	マイケル・ジョーダン	", thisWordE = "	If you quit once it becomes a habit. Never quit!	", thisPersonE = "	Michael Jordan	")
        clsWise[	14	] = ClassWise( thisWord = "	인간 세상에 실패라는 건 없다.	", thisPerson = "	사카모토 료마	", thisCategory = 	0	, thisWordJ = "	人の世に失敗ちゅうことは、ありゃせんぞ。	", thisPersonJ = "	坂本龍馬	", thisWordE = "	There is no failure in the human world.	", thisPersonE = "	Sakamoto Ryōma	")
        clsWise[	15	] = ClassWise( thisWord = "	기억해, 오늘은 어제 걱정하던 내일이야.	", thisPerson = "	데일 카네기	", thisCategory = 	2	, thisWordJ = "	いいですか。昨日あなたが心配していた明日が、今日なのですよ。	", thisPersonJ = "	デール・カーネギー	", thisWordE = "	Remember, today is the tomorrow you worried about yesterday.	", thisPersonE = "	Dale Carnegie	")
        clsWise[	16	] = ClassWise( thisWord = "	천재는 인내심이다.	", thisPerson = "	아이작 뉴턴	", thisCategory = 	0	, thisWordJ = "	天才は忍耐です。	", thisPersonJ = "	アイザック・ニュートン	", thisWordE = "	Genius is patience.	", thisPersonE = "	Isaac Newton	")
        clsWise[	17	] = ClassWise( thisWord = "	나는 여태껏 내가 배울 것이 아무것도 없을 정도로 무식한 사람을 만난 적이 없다.	", thisPerson = "	갈릴레오 갈릴레이	", thisCategory = 	1	, thisWordJ = "	何も学ぶところがないというような人物に、私はこれまで一度も出会ったことがない。	", thisPersonJ = "	ガリレオ・ガリレイ	", thisWordE = "	I have never met a man so ignorant that I couldn't learn something from him.	", thisPersonE = "	Galileo Galilei	")
        clsWise[	18	] = ClassWise( thisWord = "	당신이 하는 일을 스스로 과소평가한다면, 세계도 당신을 과소평가할 것이다.	", thisPerson = "	오프라 윈프리	", thisCategory = 	0	, thisWordJ = "	自分のすることを過小評価してしまうと、世界もあなた自身を過小評価するようになってしまうわ。	", thisPersonJ = "	オプラ・ウィンフリー	", thisWordE = "	When you undervalue what you do, the world will undervalue who you are.	", thisPersonE = "	Oprah Winfrey	")
        clsWise[	19	] = ClassWise( thisWord = "	삶은 과감한 모험이거나, 아니면 아무것도 아니다.	", thisPerson = "	헬렌 켈러	", thisCategory = 	0	, thisWordJ = "	人生とは、 果敢なる冒険か、つまらぬ物のどちらかだ。	", thisPersonJ = "	ヘレン・ケラー	", thisWordE = "	Life is either a daring adventure or nothing at all.	", thisPersonE = "	Helen Keller	")
        clsWise[	20	] = ClassWise( thisWord = "	안하고 죽어도 좋은 일만 내일로 미뤄라.	", thisPerson = "	파블로 피카소	", thisCategory = 	0	, thisWordJ = "	明日に延ばしてもいいのは、やり残して死んでもかまわないことだけだ。	", thisPersonJ = "	パブロ・ピカソ	", thisWordE = "	Only put off until tomorrow, what you are willing to die having left undone.	", thisPersonE = "	Pablo Picasso	")
        clsWise[	21	] = ClassWise( thisWord = "	꿈은 도망치지 않는다. 도망치는 것은 언제나 자신이다.	", thisPerson = "	(짱구는 못말려) 짱구아빠	", thisCategory = 	0	, thisWordJ = "	夢は逃げない。逃げるのはいつも自分だ。	", thisPersonJ = "	『クレヨンしんちゃん』野原ひろし	", thisWordE = "	A dream does not run away. It is always me.	", thisPersonE = "	(Crayon Shin-chan) Hiroshi Nohara	")
        clsWise[	22	] = ClassWise( thisWord = "	혼자 컸다고 자만하는 녀석은 클 자격이 없어.	", thisPerson = "	(짱구는 못말려) 짱구아빠	", thisCategory = 	1	, thisWordJ = "	自分一人ででかくなった気でいる奴はでかくなる資格は無い。	", thisPersonJ = "	『クレヨンしんちゃん』野原ひろし	", thisWordE = "	Those who feel they have grown alone are not eligible to grow.	", thisPersonE = "	(Crayon Shin-chan) Hiroshi Nohara	")
        clsWise[	23	] = ClassWise( thisWord = "	나쁜 일을 했을 때는 잘못했다고 말하는 거야.	", thisPerson = "	(짱구는 못말려) 짱구	", thisCategory = 	1	, thisWordJ = "	悪いことをした時はごめんなさいって言うんだゾ。	", thisPersonJ = "	『クレヨンしんちゃん』野原しんのすけ	", thisWordE = "	When you did something bad, you should say 'I'm sorry'.	", thisPersonE = "	(Crayon Shin-chan) Shinnosuke Nohara	")
        clsWise[	24	] = ClassWise( thisWord = "	한명의 여성을 계속 사랑하다니 멋진 일이잖아.	", thisPerson = "	(짱구는 못말려) 짱구아빠	", thisCategory = 	3	, thisWordJ = "	一人の女をずっと愛せるなんてカッコイイじゃねえか。	", thisPersonJ = "	『クレヨンしんちゃん』野原ひろし	", thisWordE = "	Isn't it cool that you can always love one woman?	", thisPersonE = "	(Crayon Shin-chan) Hiroshi Nohara	")
        clsWise[	25	] = ClassWise( thisWord = "	웃음이 적은 곳에서는 적은 성공만이 있을 뿐이다.	", thisPerson = "	앤드류 카네기	", thisCategory = 	0	, thisWordJ = "	笑い声のないところに成功はない	", thisPersonJ = "	アンドリュー・カーネギー	", thisWordE = "	There is little success where there is little laughter.	", thisPersonE = "	Andrew Carnegie	")
        clsWise[	26	] = ClassWise( thisWord = "	내 걸음은 느리지만, 절대 뒤돌아 걷지는 않았다.	", thisPerson = "	에이브러햄 링컨	", thisCategory = 	0	, thisWordJ = "	私の歩きは遅いが、決して戻ることはない。	", thisPersonJ = "	エイブラハム・リンカーン	", thisWordE = "	I am a slow walker, but I never walk back.	", thisPersonE = "	Abraham Lincoln	")
        clsWise[	27	] = ClassWise( thisWord = "	우리는 시간을 도구로 사용해야지, 소파로 활용해서는 안된다.	", thisPerson = "	존 F. 케네디	", thisCategory = 	0	, thisWordJ = "	私達は時間を道具として使うべきで、ソファーとして使ってはならない。	", thisPersonJ = "	ジョン・F・ケネディ	", thisWordE = "	We must use time as a tool, not as a couch.	", thisPersonE = "	John F. Kennedy	")
        clsWise[	28	] = ClassWise( thisWord = "	질까보냐.	", thisPerson = "	혼다 소이치로	", thisCategory = 	0	, thisWordJ = "	負けるもんか	", thisPersonJ = "	本田宗一郎	", thisWordE = "	I will not be a loser	", thisPersonE = "	Soichiro Honda	")
        clsWise[	29	] = ClassWise( thisWord = "	가난한 사람들은 공통적인 한 행동 때문에 실패한다. 그들의 인생은 기다리다가 끝이 난다.	", thisPerson = "	마윈	", thisCategory = 	0	, thisWordJ = "	貧しい人々が失敗するのは、1つの一般的な行動のためです。彼らの全生涯は待ち続けるんです。	", thisPersonJ = "	馬雲	", thisWordE = "	Poor people fail because on one common behaviour: Their Whole Life is About Waiting.	", thisPersonE = "	Jack Ma	")
        clsWise[	30	] = ClassWise( thisWord = "	누구나가 위대한 일을 하는 것은 아니다. 하지만 우리는 위대한 사랑으로 작은 일을 행할 수 있다.	", thisPerson = "	테레사 수녀	", thisCategory = 	1	, thisWordJ = "	誰もがみな偉大なことができるわけではありません。しかし私たちは大いなる愛で小さなことはできます。	", thisPersonJ = "	マザー・テレサ	", thisWordE = "	Not all of us can do great things. But we can do small things with great love.	", thisPersonE = "	Mother Teresa	")
        clsWise[	31	] = ClassWise( thisWord = "	내가 성공할 수 있었던 것은, 절대 변명하거나 변명을 받아들이지 않았기 때문이다.	", thisPerson = "	플로렌스 나이팅게일	", thisCategory = 	1	, thisWordJ = "	私が成功したのは、決して弁解したり、弁解を受け入れなかったからです。	", thisPersonJ = "	フローレンス・ナイチンゲール	", thisWordE = "	I attribute my success to this:—I never gave or took an excuse.	", thisPersonE = "	Florence Nightingale	")
        clsWise[	32	] = ClassWise( thisWord = "	계속한다는 것은 아주 중요하다. 당신이 강제로 그만둘 수밖에 없지 않는 이상, 포기해서는 안된다.	", thisPerson = "	일론 머스크	", thisCategory = 	0	, thisWordJ = "	持続性は非常に重要です。 あきらめる必要がない限り、あきらめてはいけません。	", thisPersonJ = "	イーロン・マスク	", thisWordE = "	Persistence is very important. You should not give up unless you are forced to give up.	", thisPersonE = "	Elon Musk	")
        clsWise[	33	] = ClassWise( thisWord = "	내가 상대를 믿는 것과, 상대가 나를 배신하는 것은 아무런 관계도 없었던 거야.	", thisPerson = "	(십이국기) 나카지마 요코	", thisCategory = 	1	, thisWordJ = "	私が相手を信じることと、相手が私を裏切ることとは何の関係もなかったんだ。	", thisPersonJ = "	『十二国記』中嶋陽子	", thisWordE = "	It has nothing to do with 'I believe someone' to 'someone betray me'	", thisPersonE = "	(The Twelve Kingdoms) Youko Nakajima	")
        clsWise[	34	] = ClassWise( thisWord = "	42	", thisPerson = "	(은하수를 여행하는 히치하이커를 위한 안내서) 깊은 생각	", thisCategory = 	2	, thisWordJ = "	42	", thisPersonJ = "	『銀河ヒッチハイク・ガイド』ディープ・ソート	", thisWordE = "	42	", thisPersonE = "	(The Hitchhiker's Guide to the Galaxy) Deep Thought	")
        clsWise[	35	] = ClassWise( thisWord = "	웃음이 없는 하루는 버린 하루다.	", thisPerson = "	찰리 채플린	", thisCategory = 	1	, thisWordJ = "	無駄な一日。それは笑いのない日である。	", thisPersonJ = "	チャールズ・チャップリン	", thisWordE = "	A day without laughter is a day wasted.	", thisPersonE = "	Charlie Chaplin	")
        clsWise[	36	] = ClassWise( thisWord = "	내 사전에 불가능은 없다.	", thisPerson = "	나폴레옹 보나파르트	", thisCategory = 	1	, thisWordJ = "	辞書に不可能の3文字はありません。	", thisPersonJ = "	ナポレオン・ボナパルト	", thisWordE = "	The word impossible is not in my dictionary.	", thisPersonE = "	Napoleon	")
        clsWise[	37	] = ClassWise( thisWord = "	너는 머뭇거릴 수 있지만, 시간은 그렇지 않다.	", thisPerson = "	벤저민 프랭클린	", thisCategory = 	0	, thisWordJ = "	君は遅れるかもしれないが、時は決して遅れない。	", thisPersonJ = "	ベンジャミン・フランクリン	", thisWordE = "	You may delay, but time will not.	", thisPersonE = "	Benjamin Franklin	")
        clsWise[	38	] = ClassWise( thisWord = "	오늘 나무 그늘에서 쉴 수 있는 이유는, 예전에 누군가가 그 나무를 심었기 때문이다.	", thisPerson = "	워렌 버핏	", thisCategory = 	0	, thisWordJ = "	誰かが大昔に木を植えてくれたから、誰かが今日、 日陰に座っていられたりするのだ。	", thisPersonJ = "	ウォーレン・バフェット	", thisWordE = "	Someone's sitting in the shade today because someone planted a tree a long time ago.	", thisPersonE = "	Warren Buffett	")
        clsWise[	39	] = ClassWise( thisWord = "	네가 누구인지, 무엇인지 말해 줄 사람은 필요 없다. 너는 그냥 너 자신이면 된다.	", thisPerson = "	존 레논	", thisCategory = 	2	, thisWordJ = "	自分がいったい何者なのか、誰かに指摘してもらう必要のある人間なんて１人もいない。君はそのままで、君なんだ。	", thisPersonJ = "	ジョン・レノン	", thisWordE = "	You don’t need anybody to tell you who you are or what you are. You are what you are!	", thisPersonE = "	John Lennon	")
        clsWise[	40	] = ClassWise( thisWord = "	일하는 법과 사랑하는 법을 알고 있는 사람이라면 누구나 이 세상을 당당히 살아갈 수 있다.	", thisPerson = "	레프 톨스토이	", thisCategory = 	3	, thisWordJ = "	仕事の仕方と愛の仕方を知っていれば、誰でもこの世を堂々と生きることができます。	", thisPersonJ = "	レフ・トルストイ	", thisWordE = "	One can live magnificently in this world if one knows how to work and how to love.	", thisPersonE = "	Leo Tolstoy	")
        clsWise[	41	] = ClassWise( thisWord = "	만약 네가 완벽함을 찾는다면, 너는 절대 만족할 수 없다.	", thisPerson = "	레프 톨스토이	", thisCategory = 	1	, thisWordJ = "	あなたが完璧を探すならば、あなたは満足することは決してないでしょう。	", thisPersonJ = "	レフ・トルストイ	", thisWordE = "	If you look for perfection, you'll never be content.	", thisPersonE = "	Leo Tolstoy	")
        clsWise[	42	] = ClassWise( thisWord = "	가장 강한 전사 둘은 인내와 시간이다.	", thisPerson = "	레프 톨스토이	", thisCategory = 	0	, thisWordJ = "	2人の最も強力な戦士は忍耐と時間である。	", thisPersonJ = "	レフ・トルストイ	", thisWordE = "	The two most powerful warriors are patience and time.	", thisPersonE = "	Leo Tolstoy	")
        clsWise[	43	] = ClassWise( thisWord = "	너 자신에 대해 믿는다면, 너는 살아가는 방법에 대해서 알게 될 것이다.	", thisPerson = "	요한 볼프강 폰 괴테	", thisCategory = 	1	, thisWordJ = "	あなたは自分を信頼し始めれば、ただちに生き方が分かるでありましょう。	", thisPersonJ = "	ヨハン・ヴォルフガング・フォン・ゲーテ	", thisWordE = "	As soon as you trust yourself, you will know how to live.	", thisPersonE = "	Johann Wolfgang von Goethe	")
        clsWise[	44	] = ClassWise( thisWord = "	우리를 죽이지 못한 것이 우리를 더욱 강하게 만든다.	", thisPerson = "	프리드리히 니체	", thisCategory = 	0	, thisWordJ = "	我等を殺せないものが我らを強くする。	", thisPersonJ = "	フリードリヒ・ニーチェ	", thisWordE = "	That which does not kill us makes us stronger.	", thisPersonE = "	Friedrich Nietzsche	")
        clsWise[	45	] = ClassWise( thisWord = "	사랑은 두 사람이 마주 쳐다보는 것이 아니라 함께 같은 방향을 바라보는 것이다.	", thisPerson = "	앙투안 드 생텍쥐페리	", thisCategory = 	3	, thisWordJ = "	愛はお互いを見つめ合うことではなく、ともに同じ方向を見つめることである。	", thisPersonJ = "	アントワーヌ・ド・サン＝テグジュペリ	", thisWordE = "	Love does not consist in gazing at each other, but in looking outward together in the same direction.	", thisPersonE = "	Antoine de Saint-Exupéry	")
        clsWise[	46	] = ClassWise( thisWord = "	행복은 생각, 말 행동이 조화를 이룰 때 찾아온다.	", thisPerson = "	마하트마 간디	", thisCategory = 	1	, thisWordJ = "	幸福はあなたが何を考え、何を言って、そしてあなたがしていることが調和している時に来る。	", thisPersonJ = "	マハトマ・ガンディー	", thisWordE = "	Happiness is when what you think, what you say, and what you do are in harmony.	", thisPersonE = "	Mahatma Gandhi	")
        clsWise[	47	] = ClassWise( thisWord = "	미래는 당신이 오늘 무엇을 하느냐에 달려있다.	", thisPerson = "	마하트마 간디	", thisCategory = 	0	, thisWordJ = "	未来は今日何をするかで決まる。	", thisPersonJ = "	マハトマ・ガンディー	", thisWordE = "	The future depends on what you do today.	", thisPersonE = "	Mahatma Gandhi	")
        clsWise[	48	] = ClassWise( thisWord = "	날지 못한다면 뛰십시오, 뛰지 못한다면 걸으십시오, 걷지 못한다면 기십시오. 무엇을 하던 가장 중요한 것은, 앞으로 나아가야 한다는 것입니다.	", thisPerson = "	마틴 루터 킹 주니어	", thisCategory = 	0	, thisWordJ = "	飛べないなら走りなさい,走れないなら歩きなさい,歩けないなら這いなさい。何をするにせよ,前進し続けなければなりません	", thisPersonJ = "	マーティン・ルーサー・キング・ジュニア	", thisWordE = "	If you can't fly then run, if you can't run then walk, if you can't walk then crawl, but whatever you do you have to keep moving forward.	", thisPersonE = "	Martin Luther King Jr.	")
        clsWise[	49	] = ClassWise( thisWord = "	용서는 가끔 발생하는 행위가 아니라, 지속적으로 지녀야할 태도입니다.	", thisPerson = "	마틴 루터 킹 주니어	", thisCategory = 	1	, thisWordJ = "	許しは時折の行為ではなく、続いて持つべき態度です。	", thisPersonJ = "	マーティン・ルーサー・キング・ジュニア	", thisWordE = "	Forgiveness is not an occasional act, it is a constant attitude.	", thisPersonE = "	Martin Luther King Jr.	")
        clsWise[	50	] = ClassWise( thisWord = "	스스로 운이 나쁘다고 생각하지 않는 한은 나쁜 운이란 없다.	", thisPerson = "	정주영	", thisCategory = 	1	, thisWordJ = "	自分から運が無いと考えない以上、悪い運と言うものは無い。	", thisPersonJ = "	鄭周永	", thisWordE = "	As long as you don't think that you are unlucky, unlucky is not exsist.	", thisPersonE = "	Chung Ju-yung	")
        clsWise[	51	] = ClassWise( thisWord = "	이봐, 해보기나 했어?	", thisPerson = "	정주영	", thisCategory = 	0	, thisWordJ = "	おい、やってみたか？	", thisPersonJ = "	鄭周永	", thisWordE = "	Hey, did you try it?	", thisPersonE = "	Chung Ju-yung	")
        clsWise[	52	] = ClassWise( thisWord = "	우리 모두는 별이고, 반짝일 권리가 있다.	", thisPerson = "	마릴린 먼로	", thisCategory = 	2	, thisWordJ = "	私たちは、みなスターで、輝く権利を持っている。	", thisPersonJ = "	マリリン・モンロー	", thisWordE = "	We are all of us stars, and we deserve to twinkle	", thisPersonE = "	Marilyn Monroe	")
        clsWise[	53	] = ClassWise( thisWord = "	나는 내가 가장 사랑하는 사람이고, 나를 가장 사랑하는 사람은 나이다.	", thisPerson = "	(아가) 솔로몬	", thisCategory = 	2	, thisWordJ = "	私は私の最愛のもの。私の最愛は私。	", thisPersonJ = "	『雅歌』ソロモン	", thisWordE = "	I am my beloved's, and my beloved is mine.	", thisPersonE = "	(Song of Songs) Solomon	")
        clsWise[	54	] = ClassWise( thisWord = "	집착을 가지지 않는 이는 근심할 일도 없다.	", thisPerson = "	석가모니	", thisCategory = 	1	, thisWordJ = "	執着を持たない人は、悩みも持たない。	", thisPersonJ = "	釈迦	", thisWordE = "	If you has no obsessions, nevermore has grief.	", thisPersonE = "	Gautama Buddha	")
        clsWise[	55	] = ClassWise( thisWord = "	사랑은 무엇보다도 자신을 위한 선물이다.	", thisPerson = "	장 아누이	", thisCategory = 	3	, thisWordJ = "	恋は、何よりも自分の為になる贈り物だ。	", thisPersonJ = "	ジャン・アヌイ	", thisWordE = "	Love is, above all else, the gift of oneself.	", thisPersonE = "	Jean Anouilh	")
        clsWise[	56	] = ClassWise( thisWord = "	사랑의 가장 강력한 증상은 견딜 수 없을 정도로 친절해지는 것입니다.	", thisPerson = "	빅토르 위고	", thisCategory = 	3	, thisWordJ = "	愛の一番強い症状は、耐えられないほどに優しくなる事です。	", thisPersonJ = "	ヴィクトル・ユーゴー	", thisWordE = "	The most powerful symptom of love is a tenderness which becomes at times almost insupportable.	", thisPersonE = "	Victor Hugo	")
        clsWise[	57	] = ClassWise( thisWord = "	사람들은 힘이 부족한 게 아니다. 그들은 의지가 부족하다.	", thisPerson = "	빅토르 위고	", thisCategory = 	0	, thisWordJ = "	人は強さに欠けているのではない。意志を欠いているのだ。	", thisPersonJ = "	ヴィクトル・ユーゴー	", thisWordE = "	People do not lack strength, they lack will.	", thisPersonE = "	Victor Hugo	")
        clsWise[	58	] = ClassWise( thisWord = "	모두에게 넌 과분하지.	", thisPerson = "	(디즈니 : 미녀와 야수 2017) 개스톤	", thisCategory = 	2	, thisWordJ = "	誰もお前より凄い人はない。	", thisPersonJ = "	『ディズニー：美女と野獣2017』ガストン	", thisWordE = "	Nobody deserves you. 	", thisPersonE = "	(Disney : Beauty and the Beast 2017) Gaston	")
        clsWise[	59	] = ClassWise( thisWord = "	아무리 기적이라 해도 시간이 좀 걸린단다.	", thisPerson = "	(디즈니 : 신데렐라 1950) 요정대모	", thisCategory = 	1	, thisWordJ = "	奇跡でさえ起こるのには時間がかかるものなの。	", thisPersonJ = "	『ディズニー：シンデレラ1950』フェアリー・ゴッドマザー	", thisWordE = "	Even miracles take a little time.	", thisPersonE = "	(Disney : Cinderella 1950) Fairy Godmother	")
        clsWise[	60	] = ClassWise( thisWord = "	절대 포기하지 마라. 절대, 절대, 절대, 절대로!	", thisPerson = "	윈스턴 처칠	", thisCategory = 	0	, thisWordJ = "	絶対に屈服してはならない。絶対に、絶対に、絶対に、絶対に！	", thisPersonJ = "	ウィンストン・チャーチル	", thisWordE = "	Never give in. Never, never, never, never.	", thisPersonE = "	Sir Winston Churchill	")
        clsWise[	61	] = ClassWise( thisWord = "	성공의 비밀은 흔히 하는 일들을 더 적게 하는 것이다.	", thisPerson = "	존 D. 록펠러	", thisCategory = 	0	, thisWordJ = "	成功への秘訣は、一般的な事を少なくする事だ。	", thisPersonJ = "	ジョン・ロックフェラー	", thisWordE = "	The secret to success is to do the common things uncommonly well.	", thisPersonE = "	John D. Rockefeller	")
        clsWise[	62	] = ClassWise( thisWord = "	어려움 속에, 기회가 있다.	", thisPerson = "	알베르트 아인슈타인	", thisCategory = 	0	, thisWordJ = "	困難の中に、機会がある。	", thisPersonJ = "	アルベルト・アインシュタイン	", thisWordE = "	In the middle of difficulty, lies opportunity.	", thisPersonE = "	Albert Einstein	")
        clsWise[	63	] = ClassWise( thisWord = "	어떤 바보라도 비판하고, 비난하고, 불만을 내비칠 수 있다. 그리고 대부분의 바보가 그렇게 한다.	", thisPerson = "	데일 카네기	", thisCategory = 	1	, thisWordJ = "	どんな愚者でも批判し、非難し、文句を言うことはできる。そして、多くの愚者がそうする。	", thisPersonJ = "	デール・カーネギー	", thisWordE = "	Any fool can criticize, condemn and complain – and most fools do.	", thisPersonE = "	Dale Carnegie	")
        clsWise[	64	] = ClassWise( thisWord = "	사랑에 빠지면 졸리지 않을 겁니다. 왜냐하면 현실이 꿈보다 더 멋지게 되거든요.	", thisPerson = "	닥터 수스	", thisCategory = 	3	, thisWordJ = "	恋に落ちると眠れなくなるでしょう。 だって、ようやく現実が夢より素敵になったんだから。	", thisPersonJ = "	ドクター・スース	", thisWordE = "	You know you're in love when you can't fall asleep because reality is finally better than your dreams.	", thisPersonE = "	Dr. Seuss	")
        clsWise[	65	] = ClassWise( thisWord = "	누군가에게 깊이 사랑받으면 힘이 생기고, 누군가를 깊이 사랑하면 용기가 생긴다.	", thisPerson = "	노자	", thisCategory = 	3	, thisWordJ = "	誰かを深く愛せば、強さが生まれる。誰かに深く愛されれば、勇気が生まれる。	", thisPersonJ = "	老子	", thisWordE = "	Being deeply loved by someone gives you strength, while loving someone deeply gives you courage.	", thisPersonE = "	Laozi	")
        clsWise[	66	] = ClassWise( thisWord = "	사람은 사랑받고 있으니까 사랑받는 겁니다. 사랑에 이유는 필요 없습니다.	", thisPerson = "	파울로 코엘료	", thisCategory = 	3	, thisWordJ = "	人は愛されているから愛されているんです。愛に理由は必要ありません。	", thisPersonJ = "	パウロ・コエーリョ	", thisWordE = "	One is loved because one is loved. No reason is needed for loving.	", thisPersonE = "	Paulo Coelho	")
        clsWise[	67	] = ClassWise( thisWord = "	나는 절망할 때마다, 진실과 사랑이 결국에는 승리해 왔다는 사실을 떠올립니다.	", thisPerson = "	마하트마 간디	", thisCategory = 	3	, thisWordJ = "	私は失望した時、歴史を通して真実と愛がいつも勝っていたことを思い出す。	", thisPersonJ = "	マハトマ・ガンディー	", thisWordE = "	When I despair, I remember that all through history the way of truth and love have always won.	", thisPersonE = "	Mahatma Gandhi	")
        clsWise[	68	] = ClassWise( thisWord = "	우리는 우리가 사랑하는 것들을 있는 그대로 사랑한다.	", thisPerson = "	로버트 프로스트	", thisCategory = 	3	, thisWordJ = "	我等は我等が愛するものが何であれとも、そのまま愛する。	", thisPersonJ = "	ロバート・フロスト	", thisWordE = "	We love the things we love for what they are.	", thisPersonE = "	Robert Frost	")
        clsWise[	69	] = ClassWise( thisWord = "	당신을 평범한 사람처럼 대하는 사람을 사랑하지 마세요.	", thisPerson = "	오스카 와일드	", thisCategory = 	3	, thisWordJ = "	あなたのことをただの凡人として扱う人を愛してはいけません。 	", thisPersonJ = "	オスカー・ワイルド	", thisWordE = "	Never love anyone who treats you like you're ordinary.	", thisPersonE = "	Oscar Wilde	")

        Log.d("wiseDGP", "[gp001][MainActivity][here] 명언 초기화")

        fun fncChangeWise(tmpClass : Array<ClassWise?>, tmpSize : Int) {
            val thisRandom = Random().nextInt(tmpSize) // [here:fncChangeWise] 명언 번호를 무작위로 뽑기 위한 Int 변수 thisRandom

            /* Language Setting */
            var txtWiseText : String? = ""
            var txtPersonText : String? = ""
            when(chkLanguage) {
                0 -> {
                    txtWiseText = tmpClass[thisRandom]?.word
                    txtPersonText = "by "+ tmpClass[thisRandom]?.person
                }
                1 -> {
                    txtWiseText = tmpClass[thisRandom]?.wordJ
                    txtPersonText = "by "+ tmpClass[thisRandom]?.personJ
                }
                2 -> {
                    txtWiseText = tmpClass[thisRandom]?.wordE
                    txtPersonText = "by "+ tmpClass[thisRandom]?.personE
                }
            }
            txtWise.text = txtWiseText
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

        /* 2019-06-09 */
        /* Language Change */
        var forTextWiseAll: String
        var forTextWiseGrowth: String
        var forTextWiseAttitude: String
        var forTextWiseHealing: String
        var forTextWiseLove: String

        var btnWiseAllText : String
        var btnWiseGrowthText : String
        var btnWiseAttitudeText : String
        var btnWiseHealingText : String
        var btnWiseLoveText : String

        fun uiLanguageChange() {
            when(chkLanguage){
                0 -> {
                    forTextWiseAll = "전체\n"
                    forTextWiseGrowth = "발전\n"
                    forTextWiseAttitude = "태도\n"
                    forTextWiseHealing = "힐링\n"
                    forTextWiseLove = "사랑\n"

                    btnWiseAllText = "$forTextWiseAll ($clsWiseSize)"
                    btnWiseAll.text = btnWiseAllText

                    btnWiseGrowthText = forTextWiseGrowth + "(" + clsCategorySize[0] + ")"
                    btnWiseGrowth.text = btnWiseGrowthText
                    btnWiseAttitudeText = forTextWiseAttitude+ "(" + clsCategorySize[1] + ")"
                    btnWiseAttitude.text = btnWiseAttitudeText
                    btnWiseHealingText = forTextWiseHealing + "(" + clsCategorySize[2] + ")"
                    btnWiseHealing.text = btnWiseHealingText
                    btnWiseLoveText = forTextWiseLove + "(" + clsCategorySize[3] + ")"
                    btnWiseLove.text = btnWiseLoveText
                }
                1 -> {
                    forTextWiseAll = "全体\n"
                    forTextWiseGrowth = "発展\n"
                    forTextWiseAttitude = "態度\n"
                    forTextWiseHealing = "治療\n"
                    forTextWiseLove = "愛\n"

                    btnWiseAllText = "$forTextWiseAll ($clsWiseSize)"
                    btnWiseAll.text = btnWiseAllText

                    btnWiseGrowthText = forTextWiseGrowth + "(" + clsCategorySize[0] + ")"
                    btnWiseGrowth.text = btnWiseGrowthText
                    btnWiseAttitudeText = forTextWiseAttitude+ "(" + clsCategorySize[1] + ")"
                    btnWiseAttitude.text = btnWiseAttitudeText
                    btnWiseHealingText = forTextWiseHealing + "(" + clsCategorySize[2] + ")"
                    btnWiseHealing.text = btnWiseHealingText
                    btnWiseLoveText = forTextWiseLove + "(" + clsCategorySize[3] + ")"
                    btnWiseLove.text = btnWiseLoveText
                }
                2 -> {
                    forTextWiseAll = "ALL\n"
                    forTextWiseGrowth = "GROW\n"
                    forTextWiseAttitude = "ATTI\n"
                    forTextWiseHealing = "HEAL\n"
                    forTextWiseLove = "LOVE\n"

                    btnWiseAllText = "$forTextWiseAll ($clsWiseSize)"
                    btnWiseAll.text = btnWiseAllText

                    btnWiseGrowthText = forTextWiseGrowth + "(" + clsCategorySize[0] + ")"
                    btnWiseGrowth.text = btnWiseGrowthText
                    btnWiseAttitudeText = forTextWiseAttitude+ "(" + clsCategorySize[1] + ")"
                    btnWiseAttitude.text = btnWiseAttitudeText
                    btnWiseHealingText = forTextWiseHealing + "(" + clsCategorySize[2] + ")"
                    btnWiseHealing.text = btnWiseHealingText
                    btnWiseLoveText = forTextWiseLove + "(" + clsCategorySize[3] + ")"
                    btnWiseLove.text = btnWiseLoveText
                }
            }
        }
        uiLanguageChange()

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

        val alertOptionTitleText = arrayOf("옵션", "オプション", "Option")
        val alertOptionItemText = arrayOf(arrayOf("명언 전환시간", "TTS", "언어"), arrayOf("格言転換時間", "TTS", "言語"), arrayOf("Change Time", "TTS", "Language"))
        val alertNeutralButtonText = arrayOf("닫기", "閉じる", "Close")

        val dialogOptionTimeTitleText = arrayOf("명언 전환시간 설정", "格言転換時間設定", "Change Time Setting")
        val alertOptionItemTimeText = arrayOf(arrayOf("10초", "1분", "10분"), arrayOf("10秒", "1分", "10分"), arrayOf("10 sec", "1 min", "10 min"))

        val dialogOptionTTSTitleText =arrayOf("TTS 설정", "TTS設定", "TTS Setting")

        val dialogOptionLanguageTitleText = arrayOf("언어설정", "言語設定", "Language Setting")

        val btnOption = findViewById<Button>(R.id.btnOption)
        btnOption.setOnClickListener{
            val alertOption = AlertDialog.Builder(this@MainActivity)
            alertOption.setTitle(alertOptionTitleText[chkLanguage])

            val alertOptionItem = arrayOf(alertOptionItemText[chkLanguage][0], alertOptionItemText[chkLanguage][1], alertOptionItemText[chkLanguage][2])
            alertOption.setSingleChoiceItems(alertOptionItem, -1) { dialogOption , i ->
                when(i) {
                    0 -> {
                        val dialogOptionTime = AlertDialog.Builder(this@MainActivity)
                        dialogOptionTime.setTitle(dialogOptionTimeTitleText[chkLanguage])

                        val alertOptionTime = arrayOf(alertOptionItemTimeText[chkLanguage][0], alertOptionItemTimeText[chkLanguage][1], alertOptionItemTimeText[chkLanguage][2])
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

                        dialogOptionTime.setNeutralButton(alertNeutralButtonText[chkLanguage]) {dialog : DialogInterface, _ : Int -> dialog.cancel() }

                        val alertDialogTime = dialogOptionTime.create()
                        alertDialogTime.show()
                    }

                    1 -> {
                        val dialogOptionTTS = AlertDialog.Builder(this@MainActivity)
                        dialogOptionTTS.setTitle(dialogOptionTTSTitleText[chkLanguage])

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

                        dialogOptionTTS.setNeutralButton(alertNeutralButtonText[chkLanguage]) { dialog : DialogInterface, _ : Int -> dialog.cancel() }

                        val alertDialogTTS = dialogOptionTTS.create()
                        alertDialogTTS.show()
                    }

                    2 -> {
                        val dialogOptionLanguage = AlertDialog.Builder(this@MainActivity)
                        dialogOptionLanguage.setTitle(dialogOptionLanguageTitleText[chkLanguage])

                        val alertOptionLanguage = arrayOf("한국어", "日本語", "English")
                        dialogOptionLanguage.setSingleChoiceItems(alertOptionLanguage, -1) { dialogLanguage, j ->
                            // Toast.makeText(this, alertOptionTTS[i], Toast.LENGTH_SHORT).show()
                            when(j) {
                                0 -> {
                                    chkLanguage = 0
                                    savCategory.putInt("currentLanguageOption", chkLanguage).apply()
                                }
                                1 -> {
                                    chkLanguage = 1
                                    savCategory.putInt("currentLanguageOption", chkLanguage).apply()
                                }
                                2 -> {
                                    chkLanguage = 2
                                    savCategory.putInt("currentLanguageOption", chkLanguage).apply()
                                }
                            }
                            uiLanguageChange()
                            wiseTTSLanguage(wiseTTS)
                            iniTimer()
                            fncChangeWise(chkWise, chkWiseSize)

                            dialogLanguage.dismiss()
                        }

                        dialogOptionLanguage.setNeutralButton(alertNeutralButtonText[chkLanguage]) { dialog : DialogInterface, _ : Int -> dialog.cancel() }

                        val alertDialogLanguage = dialogOptionLanguage.create()
                        alertDialogLanguage.show()
                    }
                }
                dialogOption.dismiss()
            }

            alertOption.setNeutralButton(alertNeutralButtonText[chkLanguage]) { dialog : DialogInterface, _ : Int ->
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

    /* 2019-06-09 */
    /* Language Setting */
    private fun wiseTTSLanguage(thisTTS: TextToSpeech) {
        when (chkLanguage) {
            0 -> thisTTS.language = Locale.KOREA
            1 -> thisTTS.language = Locale.JAPAN
            2 -> thisTTS.language = Locale.UK
        }
    }

    /* 2019-06-05 */
    /* TTS */
    private fun wiseSpeaker(toSpeak : String) {
        when (savTTSOption) {
            true -> {
                if (toSpeak == "") {
                    // if there is no text
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
