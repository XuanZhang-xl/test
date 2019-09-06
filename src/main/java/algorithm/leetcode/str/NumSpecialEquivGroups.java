package algorithm.leetcode.str;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * 你将得到一个字符串数组 A。
 *
 * 如果经过任意次数的移动，S == T，那么两个字符串 S 和 T 是特殊等价的。
 *
 * 一次移动包括选择两个索引 i 和 j，且 i ％ 2 == j ％ 2，交换 S[j] 和 S [i]。
 *
 * 现在规定，A 中的特殊等价字符串组是 A 的非空子集 S，这样不在 S 中的任何字符串与 S 中的任何字符串都不是特殊等价的。
 *
 * 返回 A 中特殊等价字符串组的数量。
 *
 *
 *
 * 示例 1：
 *
 * 输入：["a","b","c","a","c","c"]
 * 输出：3
 * 解释：3 组 ["a","a"]，["b"]，["c","c","c"]
 *
 * 示例 2：
 *
 * 输入：["aa","bb","ab","ba"]
 * 输出：4
 * 解释：4 组 ["aa"]，["bb"]，["ab"]，["ba"]
 *
 * 示例 3：
 *
 * 输入：["abc","acb","bac","bca","cab","cba"]
 * 输出：3
 * 解释：3 组 ["abc","cba"]，["acb","bca"]，["bac","cab"]
 *
 * 示例 4：
 *
 * 输入：["abcd","cdab","adcb","cbad"]
 * 输出：1
 * 解释：1 组 ["abcd","cdab","adcb","cbad"]
 *
 *
 *
 * 提示：
 *
 *     1 <= A.length <= 1000
 *     1 <= A[i].length <= 20
 *     所有 A[i] 都具有相同的长度。
 *     所有 A[i] 都只由小写字母组成。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/groups-of-special-equivalent-strings
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * TODO:待改进
 * created by XUAN on 2019/9/6
 */
public class NumSpecialEquivGroups {

    @Test
    public void numSpecialEquivGroups () {
        //String[] strings = new String[]{"abcd","cdab","adcb","cbad"};
        //String[] strings = new String[]{"a","b","c","a","c","c"};
        //String[] strings = new String[]{"abc","acb","bac","bca","cab","cba"};
        String[] strings = new String[]{"ybsozowpyg","cxwjbcqoub","ihseqkmuic","viomfeaqal","lfrcngwfrm","givraxcyjj","ymyknfazyp","jdremhnuvf","kozakgpqdv","hxfmiatnym","hsecjvsgrc","bpdqrtagrz","owiactewma","hlynqbsran","tcynknzgmu","ifgbagxkzw","pipglzccsl","scxwfoknra","ilqugcrjgw","yfafspbipc","gqkxymtphr","unywjyrrqt","hyaefmgxto","rrdiswiccz","xqlgjnmwoq","qprjmhvvmh","bzgsumbzha","xbqbhgrovl","felfygphos","uoqhdkkffa","jiewpcxauv","ylunafkgqe","ylsfwwilij","slrvmcdfmq","hectnotkpr","ftpsbwksbx","keiergtica","dazlcudbti","jwaifcdzdn","hrcycayjao","mphmdnsehj","avcoarrjtu","wioftnpjqc","mnvmllpovr","dglxprxcmx","exdfkyzosy","nvyddzjnhv","fptmykwtxi","niondgvgnx","tjrydouuvp","tfovrxjbak","vikheyckxa","bbatiuqboo","chwjbpdnqo","wrnpjkdjsv","uxpxxeybsp","ixgkqlaosm","tnezyfvbeh","pjevugctqn","pkbfokvfvh","idorvbuqwi","umuuzhbqer","vtdsmabncl","jolgfdubcc","tcxgeqnali","lvpltfwoyy","iehdawfxpa","jyofluazxl","khfvpwohqn","sszugyuorg","qadthcqqry","nfergmkgdm","ufallamxvj","uszqqkmnso","izunbvnxmn","dlhoauqpst","tzqdastgcc","nnbvtnzzrj","xpilgxduwl","znhtyigiug","ieyciwnwst","mvfnpwkkkf","fvsfigebxd","irsaawbfga","bollqukrkn","kfoenbbmmg","iutmvyxgjr","jjvnntstrq","pbpsxhgxem","iwomfckpkj","zkexnaqnis","wqysgmeoju","filfezszib","jmgfgkebrh","msxpusmwfo","bzsebwltlx","yunsoggygf","zvqaqkkzoz","vezofskotu","dhkpqnjkez","ehgoqpijmf","ovlofvxtru","ftmkindcnu","eilqlotwio","mthugirhhe","qclvbzcedn","bntuxoqzpd","fhzgifwtty","trkjgskmoo","nctjigwzjd","tzxrjqiayl","kvbcvtfwlz","lrpanzifjs","pnouchfvyw","zoijujjnak","cnydcrljzc","lenxffulxs","sthkauvbae","gvccrpmusv","cogiaqqdxt","ozuwapbbum","kttwsvtrhq","zdmtjsxggk","axleeigvwv","ctmwoelbia","wyvktnjavp","agpzzhrcza","kdnnjzeabm","ehuttxikth","wvlqkpyela","bdqxozusox","ktyudhlkkp","pcrkmduish","wnnafymlfe","iaopknxmqs","yujucaufyi","wtlaycctrl","rjhqcrjnjz","qxnlmavmnz","syjggfftpr","ukdiltaghn","secouahwsf","fmoifccgag","mtxoaqzrve","yepyrgvrcu","pnwdggoqbk","mhbnppvwqr","dzvptqzcnx","dkpykdmltx","lbylnmwtah","mpucjjwomc","dhhceaybiq","uqrogpwseh","srvuwgnvpn","ayvmteaxat","dckljhocwf","gbjwmmsryj","yddkpahdfw","nbighllfkv","pveavthryz","xlloteserm","vplsdhyszv","cnoggggtpf","ybphzaskfz","jihexsomif","vjyftokxaf","hrqommecqk","nppjeueynl","metamnfwhg","whmgjbvnej","ldnkmdzcwx","zmirdnaaik","lyzplgfmex","jzkofxmjal","qhhaamtioc","akrhyhxslp","natltkvapc","gadcrppjzr","zylywnzxzn","kxppbzjpwo","uzdofnhewk","lavbadojfa","qrsobtifin","talcjjukns","xpzaojfhbu","loafhribll","kwbnjjneje","skkyxzwalb","ahiozeixrs","dpreonnmdt","pbxwkzvxdh","xtyoweclmu","weuedwqzfi","anmmleczor","hralwhvqoe","qrkrtioyyc","gukilvmoys","hkucfmshsl","ahpxjhnbxq","baqpkpoicu","oowhzjsaws","mnxvgmktoq","ymgpvqzhgy","tyewnjwzbw","rznmnpkloi","lylpeqjuzp","brumuqzueh","safhybzzpk","ysgfognugy","fwpvohknqh","achmtaohqi","ikaaimzndr","zpjulqeylp","butoqnxdpz","citgiakere","icqhmeiusk","lnowxqjqmg","ajjozjunik","xocdatqqgi","gcmvcvsurp","czpglcplsi","cargiekite","layvwqkelp","vozofuteks","iihsjfoexm","ymapykyfnz","uvnxbnmzin","mpjcujmowc","pntzbuqoxd","axrhzeisio","szizeilbff","zoinujjjak","vbphkwdzxx","vqrtntsnjj","jokjalmzfx","dhvpyslvzs","mqlqjgonxw","cyvixkeakh","cgocfiamfg","lrhllbioaf","lwlotiiqeo","pwbsksbtfx","wusrnvpnvg","nnyzdvhdjv","movrplvnlm","kadqpvzokg","lfeifzsbiz","xxleusnlff","ktdhyukplk","enzknaisqx","kokstmgroj","fewkhzdnuo","unmnnxbziv","sifbizlfez","egjhwbvjmn","wclfrfrgnm","ywslilwfij","vozefstoku","mxlfvjuaal","aqoealvmfi","piszlcpgcl","qeuzwedwfi","unzkmosqqs","rnnztvbnzj","ktmqgvxnom","nvbnuxmniz","ndwxmclkzd","acsfyppfbi","jjrqvnntst","jucuuayiyf","uzoxqdbsox","dhkultypkk","oamectlwib","gqgmyhzyvp","vntbezyfeh","lzaljyofxu","liizebffsz","cilcsgplpz","nntjbnzzrv","leslrmtexo","wjndtzicjg","uubruhemzq","thtxetihuk","zxisreihao","bzrjnvzntn","oexijmhsif","tvylwopfly","uxljvaalmf","azbprqrgdt","qzqvozkazk","rciidzcrsw","ffebszizli","mxlrxcdgpx","xwpbvzkxdh","btvacsdnml","apsthodlqu","jauuyuyfci","xarofwkcsn","vqolhharwe","jkajzjuoin","rfwcngrmlf","drjknjwpsv","gvyslumiko","nxogdinnvg","jebejnkjnw","dszpvhyvls","gynfosguyg","xuazlljfoy","jjkoflmzax","dpmmhesjhn","eyvhxikack","ixthuttkeh","tpjkvyvnwa","ebjmrhgfgk","mgtefamwhn","yhvvdslszp","xldlipwugx","sfwwilylij","mnbzinuvnx","eziflzsbfi","uzowabbmup","hsjexfimoi","mnjhegvjwb","wfrmlfngrc","jokxflmzaj","xbzgakiwgf","nvsnvrpgwu","amfeoqalvi","alhflrlbio","suapqlhodt","kotevuzofs","feukhzdnwo","mfllvjaxua","anlzmecmor","uxvfmallaj","kozefsvuto","yucfyiuaju","ywicstnwie","weukdohnfz","gyffjgstpr","mpruscgvcv","uoikjjzjan","sjgwmrybjm","ukzojjanij","cnggptoggf","ehebvnyftz","obatioqubb","slanybqnhr","fvpnowyhcu","redtdmonnp","ajzkjjuoin","lexesmrotl","jjanzkuoij","yygfgsnuog","cmfgacoifg","xuwpdlgxil","yfgyguosng","leypwqkvla","tzngicjjwd","nvspwkdrjj","vhrendmujf","atheauvksb","wgpqodbkgn","dgognxvnni","aiamoqvefl","pywfyolvtl","xnpuboqztd","ybjwmrsjgm","bgwqongdpk","dvyslhzsvp","uehnwzfodk","emuubqurzh","oifeamaqvl","jurdmfnevh","yknmafyzyp","slwjiwyfil","uidgathkln","ggstjrffpy","kklhyuktdp","hnuzwefodk","doauqlspht","ypkalelvwq","driwsiccrz","gquprseowh","fhqhowknpv","fytoaxhmge","rikpnmnzol","ytneiwswic","bhumureuzq","sphldoatqu","erqmqkmohc","ennakmjdbz","afzbikxggw","rwfnkascxo","umapbzuwob","axfjjomzkl","stpgjrgyff","pxxbkzvhdw","pynunlejep","yucfuayiju","nmnpkloirz","wnbqodggpk","akijzojjun","celaobmtiw","ajznuoijjk","uhgqeowprs","jfrhnevumd","qzbotdpuxn","nfypymakyz","wohkdzunfe","nfggguyyos","abifzkxwgg","arllloifhb","rssyzugoug","xqjwlnoqmg","swkcrnxafo","wwtzbwejny","lxzyzyznwn","xbpzkhvxdw","bcpfyfsiap","ombzabupuw","ffmnkvpwkk","zwifagxkgb","vhmpqjmvrh","tmyafnhmix","zofutskevo","ezehvnyftb","iwnciwstye","uszqmoqksn","toxllermse","ybeciahhdq","kyxackvhei","ifylwjiwsl","lznrjaifps","maulvxajlf","gynfyuosgg","vjuaafmllx","ypyfnkymaz","aoqlhudtsp","aujzoylfxl","xfzkgbiwag","zsieroahix","fixttpymwk","kulpdhytkk","mprhvhqvmj","illvngkbhf","mndlvtcabs","gldpxuixwl","tynwwjezbw","pbfhzzskya","saxnfwkcro","gvmskuyilo","arvijycjgx","sthbvuaeak","omjihsxeif","wjshoawozs","ltdghiunak","gofetxamhy","gmovmtxqkn","wxgpdlxliu","zkawggibxf","kpomnznirl","lormsexlte","tpjkwyvnva","ecqkqohmmr","gsyuoynfgg","roieixahzs","itiescywnw","ifhoablllr","lvlekqwpya","igtzwdjjnc","ehzmurbuuq","gbakzwifxg","etcaowmwia","dowehkuzfn","eakmnzbnjd","nrdvwkjpsj","gusyrszoug","ttxkymwpfi","nfvujhrdme","hkfddapwyd","nmezbnjakd","zjsowhoaws","wlnafyfemn","vluxmfaalj","rltoxesmle","xkagifgwzb","jmyjmwsrgb","nuggofgyys","rucvaotraj","inbvnzmnux","gumikolvys","xpyhrslhak","xzkwpxdhvb","cuyauijfyu","oxbdoxusqz","riiaketecg","tmyminhxfa","eycaxikkvh","fmtxhaynim","lzscpgpicl","bpkioucaqp","ngpuwrvvsn","uqbruhemzu","auxzlfjyol","qzagcstctd","xxypsbpeux","rtapdzbqrg","veomaqalfi","vfehtbezyn","ojfmipkckw","rmngwclfrf","ytkpkudklh","dgkapvkqzo","lfaohllrib","ggakzfiwxb","wkdjsrnpjv","icomkjkwfp","omiskpqaxn","ygspybzowo","mcmcuojpwj","dvpgkazokq","vhzsdplvys","uqzuuhbmer","zhuuurbqem","xtciadqqgo","ubqxwjbocc","hytoaegxfm","jgowxqlqmn","izsflbezfi","woohsszawj","cvbeqclzdn","nsgyofyggu","itbriosnqf","sgcipzpcll","cfjiyuyauu","odaalafjvb","xqmglqonjw","lfujvamxal","kozovstefu","uflxvjmlaa","bzxoqupntd","nvdpsjwkjr","qnsfboirit","njrtvtjnsq","gskvlomuyi","yygmghvpzq","kvlalqwpye","yklukhdpkt","osozqdbxux","wdirvqoiub","qzpubotnxd","fatximymhn","dicwscrzir","kxaojlfjmz","ajjkioznuj","oqudiivbwr","rrjzjqcnhj","nnbjzvtzrn","hnhemmdpsj","zjuojnikaj","lfaamxvjul","wcnfrfrmlg","ojvdaalbfa","zyghgqvmyp","aqrzrpdgbt","jycjaxvigr","nuoggsgfyy","ijzjanuojk","wnuzdofkhe","lvlekaypwq","oxqzuxodbs","ikenqsnazx","mnfahgmetw","ioeilqlotw","ezlisbizff","rhvvmhqjmp","vvsuwrpnng","zovofukets","nzbjzntvrn","hwixpefaad","sohuqlapdt","zsvotekufo","rtstjqvjnn","uqvbwriiod","abqtouibbo","wwijslylif","fziblzefsi","uzoxodbxqs","zgkvdopakq","flnxleufxs","bhumeqzruu","ufxellfsnx","jlkoajmxfz","nnbztnrvzj","yqgpzygmvh","aurjcravto","juyiuayucf","ljvlaamfux","tzxdqnbupo","fuowchyvpn","uqbuehzmur","aegmtofxhy","aklsyhrpxh","tjezwwbwny","vfbkvkpfoh","inhmfatmyx","bupntdxzqo","ounggfyygs","yksbpafhzz","ytfpxiwktm","jmrfgbehgk","xwdxkzvhpb","qlhodpaust","wedzfounhk","qmskalgxio","vnngnxdiog","ktkplkyudh","ymixhnfatm","dkyukpkhlt","kwvxpbxzdh","breuuhzqum","bqehumzruu","vtkwbvlcfz","swyjwlifil","zgiwabxkgf","zdldnxwcmk","vlmmnzqxna","guilxpdxwl","jtvjrnsqnt","eplqzujply","trkjgmosko","rnzznnbvtj","qwkhphfnov","kgteierica","qvmpmjrhvh","bqpgwkongd","wfrflmrgnc","jyxfazoull","nuoggsyygf","inzjukjoaj","scplcglipz","koyvgimuls","rocrajautv","wakvlqleyp","yfqeknalug","dohpquatsl","fzwedoukhn","svnpwjjkdr","sawjwooszh","rikaiecgte","gyaxcjvrji","wfylpvlyto","eixachkkvy","iwleotmacb","pzvthvyrea","saawgabfir","nxnnviogdg","vxphkbdwxz","kuloyvgims","oabuqikpcp","teaygofxhm","daznimakir","vgjnmjebwh","kmgmnrdfeg","kqzokgdapv","torvajcuar","uzopamubbw","uwopabbzum","mcnxzdlkwd","vbpxdhxzkw","agfmfiogcc","aknfypyzym","lgpzpiclsc","yvpreavzht","mhvjqhmprv","vokfajyftx","quaohtdpsl","vjnqrtjtsn","kofcswxnra","uavfljaxml","fotezovsku","fnuewzhkdo","irriswdzcc","qdpzxubnto","fxhnymtmia","ictgjdwjnz","wdirviuboq","amfeaivqol","uqeruhzmbu","nrsjwkjpdv","apbcsfyfpi","jplyeqluzp","zgabxfgkiw","bbaoouitqb","hedkwzunfo","jytnvpvkwa","izbxnnmnuv","viehckxaky","vuksfetozo","ijujanjozk","kvzgkqdapo","kvlelpwayq","jkvptyvnwa","wdnztgjjic","jdmhrevfnu","tminhxyafm","mpdmsjhehn","txfaimhnym","iwkpkcfmoj","phesxxpbgm","haqcqyrqdt","akunjoijzj","tlwfyopylv","zqbrumuueh","ukmqzoqssn","aotuajcrrv","goaqcdqixt","hnfzdeukwo","qlkfynugae","znuojjijak","nxvgonnidg","pkfaybszzh","iapxadhwfe","hustqoapdl","lamxujvlaf","qdxnpubotz","lluxvfmaaj","aorravtucj","nxivunbnmz","jjwompucmc","xzkwvxdhpb","aoiuqtbbob","jenuvhmdrf","cbqjbcuxwo","pufvchonyw","fedohzwnuk","ikujjoanzj","kxvzxhdwpb","bprtdgaqrz","ypdsvhzvls","bqrtrgazdp","selwbzlxbt","hnweuzdkfo","vingdgoxnn","llroxmtese","ifhvnblgkl","gysurgzsuo","zkijjnujao","sarcfwxnko","xljflyazou","yfslilwwij","bbobqtioau","ogccfgamfi","cadlbnmsvt","lyzpeqlujp","zuusgosyrg","fnkoraxwsc","rjarcvtoau","fckarosnxw","tmxelorlse","npotdnredm","ektxuhttih","tnvyvkwpja","usxomsmwfp","vjtfyxkoaf","gmfxtehoay","qohudpstal","fbizefsilz","gysuuszorg","bzuabshmgz","uxodqzbxos","itnwicswye","tjcvrraoau","wxboubcjqc","dgazbqrprt","yuyfcuuaji","yxtfvjafko","mtvsbndlca","zosoypygwb","nvwusrvnpg","xxlcpgmrdx","rgnfrmlcwf","sykzwalbxk","ajlamfuxvl","bqonggpdwk","cqrnjzhjjr","vnwhjjmbeg","vzebefyhtn","vpzvlhdsys","rmnflfrgwc","dlaoqthpsu","qanxmlvmnz","rugyzsugso","lylqzpjuep","umbzopuwab","hycoyacjar","ozupambwub","ozaujllfxy","dhfoqkkfua","pqnbahjxxh","dlquhpstao","fwcuynohpv","knafygueql","lueqzpjylp","nzunixbnmv","aokftfyjvx","fvkwpnqhoh","ypaknfymyz","nzvxnmqlma","drinzmikaa","fascroxnkw","intmfxyahm","gfnsggyuoy","jjuomcwpmc","mnfcnudkit","dhkkkuytlp","khvkxieacy","oehmiixfjs","wpyittfmxk","qydcqqhtra","amaeayvttx","uoajinzjjk","inhxtmyafm","tfvxyokfaj","jgvbejwhmn","jgecrvhssc","kalqlpyvwe","kwjejnnjbe","sbizffezli","emxbgxpsph","hkerqomcqm","lvvpzsdsyh","lxprmxxgdc","ricrsziwdc","nanlmxqmvz","jrskwvnpdj","zjaojnujik","sacosfhwue","qlhbxorbvg","qhsumcikie","wvsunrvnpg","vjayjrcxgi","kgueynafql","hedjsphmmn","jwoqlqxgmn","tmiayxhmfn","vvrpmhqjmh","jzayxloflu","otruxvlofv","ebenthvzyf","lyofaujlxz","vqamflaioe","rumejhnfvd","icneywswit","plpccilgsz","qvpwfhohkn","ixnvmzbnun","iwsjwlylif","cheakkvyxi","woyosbygzp","yyognsgugf","lrmovnvmpl","lhasrkxhyp","irinamdazk","nyougsgfyg","qvphknfhow","sbyexpuxpx","xywzsblakk","naqzvxnlmm","owmtiaeacw","ongkbgpdwq","wjzowhossa","xslxueflnf","pwfnkvohqh","fgocagcmfi","foovltrvxu","akinuojjzj","ywmbjmgjsr","mvmjqhrhvp","lasywzkbxk","pnqvohfhkw","buxnqopdtz","gssouyzgru","maaxlfvluj","hiomjeifxs","gyofyunggs","inimaadkzr","snkaxorwfc","babsuzhmgz","icdrswcirz","sgjfpygrft","ypgygqzmvh","dtyplhkkku","tnpdxzqubo","okgqpnwgbd","fnphkvohqw","vyjawktpvn","kewqyvlpla","tnnjbzznrv","lweotilqio","qatcohaihm","nfyugsgyog","jcssevrghc","icybdqhaeh","cxuowjbbqc","phoeyffgls","bpsfafpcyi","ypbfafpisc","dzrgbqrpat","ouxhbjzpfa","nsyugfoygg","kxvwdhpbxz","joijunajzk","zotuvoksfe","vjgrcyjxai","xwfnscrako","tekgiecari","vnclmtbads","wybwewtznj","yfgsoyggnu","dewnhkuzfo","ubomupazbw","msusfoxwmp","vhgyypzqgm","xgnitaeclq","fvpnkwqhoh","wplaleyqkv","kvqzzzoaqk","oridvqwbui","yfakyzypnm","uadffkkhqo","mkhoqmqcer","xdpzqnbuto","pnqztobuxd","uefkhzwodn","yrvvetpzha","pvohqhfwkn","qdpzbntoxu","uxajlavlmf","nndpdmrtoe","qvfnphohkw","szybfapkzh","ueafqlygkn","bcsiafppyf","nfysguggoy","fdjgloubcc","ipwuxldlgx","hedoukfzwn","pvfhonkwqh","bzagrpdqrt","zzqvkzoaqk","iolitoeqlw","rvscjshgec","layplvkewq","ndkzemjabn","slqoixgkam","rggyzuuoss","qgtcadtzcs","wqkelvlpya","xdjtgszgmk","iezxasroih","ssqquomnzk","kzqazzovqk","ubwdvioqir","qkmrhcemqo","yvdzhnndjv","ptyavvhrez","ozkazkqzqv","jpdksvwrnj","ixaeiszhro","alfxkojjmz","imqxsoakgl","qtdpalhuso","wgrfrmlcnf","mxqzvlnanm","imfxyntahm","iaaehxpdfw","yaciuuyujf","rdjfmuvenh","aivefmolaq","zndraaikim","kugimoyslv","gyamhefxto","ebszliizff","kvlewpyalq","pvynfwouch","gsngoyguyf","wyzyzxznln","znbjrznntv","toajkfvfyx","hbicehdqya","uqwpehgsro","lxbzbeltsw","jalsnrizpf","dkkukpltyh","sahesocfuw","pkvkofvhbf","lalvwekpyq","dtnmopdnre"};
        //List<List<String>> strLists = numSpecialEquivGroups(strings);
        //System.out.println(JSONObject.toJSONString(strLists));
        System.out.println(numSpecialEquivGroups(strings));
    }

    private int numSpecialEquivGroups(String[] strings) {
        if (strings == null || strings.length == 0) {
            return 0;
        }
        // 根节点
        List<Integer> roots = new ArrayList<>();
        // 第0个元素直接作为根节点
        roots.add(0);

        for (int i = 1; i < strings.length; i++) {
            String newStr = strings[i];
            boolean unique = true;
            for (Integer root : roots) {
                String rootString = strings[root];
                if (isSpecialEqual(newStr, rootString)) {
                    unique = false;
                }
            }
            if (unique) {
                // 新根节点
                roots.add(i);
                ArrayList<String> list2 = new ArrayList<>();
                list2.add(newStr);
            }
        }
        return roots.size();
    }

    /**
     * 判断是否特殊相等
     *
     * 1. 奇数位上的字符种类完全一样
     * 2. 偶数位上的字符种类完全一样
     *
     * @param newStr
     * @param rootString
     * @return
     */
    private boolean isSpecialEqual(String newStr, String rootString) {
        if ("".equals(newStr) && "".equals(rootString)) {
            return true;
        }
        if (newStr.length() != rootString.length()) {
            return false;
        }

        int length = newStr.length();
        char[] sorted1 = new char[length];
        char[] sorted2 = new char[length];
        int index = 0;
        for (int i = 0; i < length; i = i + 2) {
            sorted1[index] = newStr.charAt(i);
            sorted2[index] = rootString.charAt(i);
            index++;
        }
        Arrays.sort(sorted1, 0, index);
        Arrays.sort(sorted2, 0, index);
        if (!new String(sorted1).equals(new String(sorted2))) {
            return false;
        }
        int temp = index;
        for (int i = 1; i < length; i = i + 2) {
            sorted1[index] = newStr.charAt(i);
            sorted2[index] = rootString.charAt(i);
            index++;
        }
        Arrays.sort(sorted1, temp, index);
        Arrays.sort(sorted2, temp, index);
        return new String(sorted1).equals(new String(sorted2));
    }
}
