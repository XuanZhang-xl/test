var arr_img = new Array();
var page = '';
eval(fun);

/**
 * 动漫之家 获得  pages参数的代码, pages参数存储了某漫画某一章节的所有图片链接,一般有十几张
 * @type {string}
 */
var fun = function (p, a, c, k, e, d) {
    e = function (c) {
        return (c < a ? '' : e(parseInt(c / a))) + ((c = c % a) > 35 ? String.fromCharCode(c + 29) : c.toString(36))
    };
    if (!''.replace(/^/, String)) {
        while (c--) {
            d[e(c)] = k[c] || e(c)
        }
        k = [function (e) {
            return d[e]
        }];
        e = function () {
            return '\\w+'
        };
        c = 1
    }
    ;
    while (c--) {
        if (k[c]) {
            p = p.replace(new RegExp('\\b' + e(c) + '\\b', 'g'), k[c])
        }
    }
    return p
}('g f=\'{"e":"d","h":"0","i":"l\\/1\\/3\\/4\\/k.2\\r\\5\\/1\\/3\\/4\\/j.2\\r\\5\\/1\\/3\\/4\\/c.2\\r\\5\\/1\\/3\\/4\\/6.2\\r\\5\\/1\\/3\\/4\\/7.2\\r\\5\\/1\\/3\\/4\\/b.2\\r\\5\\/1\\/3\\/4\\/a.2\\r\\5\\/1\\/3\\/4\\/9.2\\r\\5\\/1\\/3\\/4\\/8.2\\r\\5\\/1\\/3\\/4\\/m.2\\r\\5\\/1\\/3\\/4\\/A.2\\r\\5\\/1\\/3\\/4\\/B.2\\r\\5\\/1\\/3\\/4\\/n.2\\r\\5\\/1\\/3\\/4\\/z.2\\r\\5\\/1\\/3\\/4\\/y.2\\r\\5\\/1\\/3\\/4\\/D.2","C":"x","w":"q","p":"\\o\\s \\t\\v\\u\\E"}\';',
    41,
    41,
    '|chapterpic|jpg|27248|110115|nimg|15556398241863|15556398245871|15556398262574|15556398258494|1555639825414|15556398250061|15556398237382|87699|id|pages|var|hidden|page_url|15556398232149|15556398226218|img|15556398266073|15556398279736|u7b2c62|chapter_name|68||u8bdd|u6240|u4e4b|u7231|chapter_order|16|15556398287837|15556398283547|15556398270829|15556398275287|sum_pages|1555639829216|u4eba'.split('|'),
    0,
    {})