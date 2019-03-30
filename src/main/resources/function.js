"use strict";

var timeCounter = 0;
var schResults = document.getElementById("schResults");
var schInput = document.getElementById("schInput");
//var watchFace = "elevenntsix|sevenfourty|twelvehotwo|eightoneten|threertnine|fivefifteen|twentypastl|fourthytenv|fiftythirty|clockfourty|gtwofiveten";
//var watchFace = "seleventhree|twelvetwotye|sponeuaeight|nineefhfivek|sixfourtenty|sevenfitenee|fortyiclocky|thfpastwenty|vfiftyhretwo|twelvegeight|ftqthirtyock|fiveffifteen";
var watchFace =   "twentytyckh|setentwfive|quarterhalf|fouppasttoh|twelveseven|nineeightfv|oastfourten|fourfiveive|sixeleveneq|threetwoone|clocknclock";
var times;
var names;

var SETTINGS = {
    resultsLimit: 100
};

function debounce(func, wait, immediate) {
    var timeout;
    return function() {
        var context = this, args = arguments;
        var later = function() {
            timeout = null;
            if (!immediate) func.apply(context, args);
        };
        var callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func.apply(context, args);
    };
}

var debouncedBuildResults = debounce(
    function(e) {
        schResults.innerHTML = "";
        //if (e.target.value.length < 3) {
        //    return;
        //}
        //for (var i = 0; i < SETTINGS.resultsLimit; i++) {
            buildResults(e.target.value, watchFace);
        //}
    },
    250
);

//Takes a string and a query and where is finds a substring that matches the query (wrapped in a regex to prevent matches in tags) it wraps it in a span and returns the new string. Where no match it just returns the original string
// @param  {string} - any string of text
// @param  {query} an array containing the text(s) to match against
// @return {[completedString]} The revised string if matches found or the original string if not

function highlightMatchesInString(string, query) {
    // the completed string will be itself if already set, otherwise, the string that was passed in
    var completedString = completedString || string;
    query.forEach(function(item) {
        var reg = "(" + item + ")(?![^<]*>|[^<>]*</)"; // explanation: http://stackoverflow.com/a/18622606/1147859
        var regex = new RegExp(reg, "i");
        // If the regex doesn't match the string just exit
        if (!string.match(regex)) {
            return;
        }
        // Otherwise, get to highlighting
        var matchStartPosition = string.match(regex).index;
        var matchEndPosition = matchStartPosition + string.match(regex)[0].toString().length;
        var originalTextFoundByRegex = string.substring(matchStartPosition, matchEndPosition);
        completedString = completedString.replace(regex, `<mark class="sch-Result_Highlight">${originalTextFoundByRegex}</mark>`);
    });
    return completedString;
}

schInput.addEventListener(
    "click",
    function(e) {
        console.log("asdfasdf");
        debouncedBuildResults(e);
    },
    false
);

function indexOfGroup(match, n) {
    var ix = match.index;
}

function buildResults(query, itemdata) {
    // Make an array from the input string
    query = query.split(" ");

    while (schResults.firstChild) {
        schResults.removeChild(schResults.firstChild);
    }

    var detail = document.createElement("p");


    var timeoutput = itemdata;
    var highlight = "";
    var hours = Math.floor(timeCounter / 12);
    var minutes = timeCounter % 12;

    var timePatterns = times[hours][minutes];
    //var regex = new RegExp(time, "i");

    var matches;
    for (var i = 0; i < timePatterns.length; i++) {
        var timePattern = timePatterns[i];
        var regex = new MultiRegExp(timePattern);
        matches = regex.exec(timeoutput);

        if (Object.keys(matches).length > 0) {
            break;
        }
    }

    //var matches = timeoutput.match(regex);
    if (matches) {
        var lastMatchEnd = 0;
        var table = document.createElement("table");
        table.classList.add("watchface");
        var tableLine = document.createElement("tr");
        table.appendChild(tableLine);
        for (var key in matches) {
            var match = matches[key];

            if (key % 2 == 0) continue;

            var matchStartPosition = match.index;
            var originalTextBeforeMatch = timeoutput.substring(lastMatchEnd, matchStartPosition);
            tableLine = addCharsToLine(tableLine, originalTextBeforeMatch, false);

            var matchEndPosition = matchStartPosition + match.text.length;

            tableLine = addCharsToLine(tableLine, match.text, true);

            lastMatchEnd = matchEndPosition;
        }

        if (lastMatchEnd != timeoutput.length) {
            var originalTextAfterMatch = timeoutput.substring(lastMatchEnd);
            tableLine = addCharsToLine(tableLine, originalTextAfterMatch, false);
        }
    }

    detail.appendChild(table);
    schResults.appendChild(detail);
    timeCounter++;
}

function addCharsToLine(tableLine, chars, highlight) {
    for (var x = 0; x < chars.length; x++) {
        var char = chars.charAt(x);

        if (char == "|") {
            var table = tableLine.parentElement;
            tableLine = document.createElement("tr");
            table.appendChild(tableLine);
        } else {
            var cell = document.createElement("td");
            cell.innerHTML = char.toUpperCase();
            if (highlight) {
                cell.classList.add("highlight");
            }
            tableLine.appendChild(cell);
        }
    }

    return tableLine;
}

times = [
    [
        ["(.*)(twelve)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(twelve)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(twelve)(.*)"],
        ["(.*)(quarter)(.+)(past)(.+)(twelve)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(twelve)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(past)(.+)(twelve)(.*)"],
        ["(.*)(half)(.+)(past)(.+)(twelve)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(to)(.+)(one)(.*)"],
        ["(.*)(twenty)(.+)(to)(.+)(one)(.*)"],
        ["(.*)(quarter)(.+)(to)(.+)(one)(.*)"],
        ["(.*)(ten)(.+)(to)(.+)(one)(.*)"],
        ["(.*)(five)(.+)(to)(.+)(one)(.*)"],
    ],
    [
        ["(.*)(one)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(one)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(one)(.*)"],
        ["(.*)(quarter)(.+)(past)(.+)(one)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(one)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(past)(.+)(one)(.*)"],
        ["(.*)(half)(.+)(past)(.+)(one)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(to)(.+)(two)(.*)"],
        ["(.*)(twenty)(.+)(to)(.+)(two)(.*)"],
        ["(.*)(quarter)(.+)(to)(.+)(two)(.*)"],
        ["(.*)(ten)(.+)(to)(.+)(two)(.*)"],
        ["(.*)(five)(.+)(to)(.+)(two)(.*)"],
    ],
    [
        ["(.*)(two)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(two)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(two)(.*)"],
        ["(.*)(quarter)(.+)(past)(.+)(two)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(two)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(past)(.+)(two)(.*)"],
        ["(.*)(half)(.+)(past)(.+)(two)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(to)(.+)(three)(.*)"],
        ["(.*)(twenty)(.+)(to)(.+)(three)(.*)"],
        ["(.*)(quarter)(.+)(to)(.+)(three)(.*)"],
        ["(.*)(ten)(.+)(to)(.+)(three)(.*)"],
        ["(.*)(five)(.+)(to)(.+)(three)(.*)"],
    ],
    [
        ["(.*)(three)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(three)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(three)(.*)"],
        ["(.*)(quarter)(.+)(past)(.+)(three)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(three)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(past)(.+)(three)(.*)"],
        ["(.*)(half)(.+)(past)(.+)(three)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(to)(.+)(four)(.*)"],
        ["(.*)(twenty)(.+)(to)(.+)(four)(.*)"],
        ["(.*)(quarter)(.+)(to)(.+)(four)(.*)"],
        ["(.*)(ten)(.+)(to)(.+)(four)(.*)"],
        ["(.*)(five)(.+)(to)(.+)(four)(.*)"],
    ],
    [
        ["(.*)(four)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(four)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(four)(.*)"],
        ["(.*)(quarter)(.+)(past)(.+)(four)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(four)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(past)(.+)(four)(.*)"],
        ["(.*)(half)(.+)(past)(.+)(four)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(to)(.+)(five)(.*)"],
        ["(.*)(twenty)(.+)(to)(.+)(five)(.*)"],
        ["(.*)(quarter)(.+)(to)(.+)(five)(.*)"],
        ["(.*)(ten)(.+)(to)(.+)(five)(.*)"],
        ["(.*)(five)(.+)(to)(.+)(five)(.*)"],
    ],
    [
        ["(.*)(five)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(five)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(five)(.*)"],
        ["(.*)(quarter)(.+)(past)(.+)(five)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(five)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(past)(.+)(five)(.*)"],
        ["(.*)(half)(.+)(past)(.+)(five)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(to)(.+)(six)(.*)"],
        ["(.*)(twenty)(.+)(to)(.+)(six)(.*)"],
        ["(.*)(quarter)(.+)(to)(.+)(six)(.*)"],
        ["(.*)(ten)(.+)(to)(.+)(six)(.*)"],
        ["(.*)(five)(.+)(to)(.+)(six)(.*)"],
    ],
    [
        ["(.*)(six)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(six)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(six)(.*)"],
        ["(.*)(quarter)(.+)(past)(.+)(six)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(six)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(past)(.+)(six)(.*)"],
        ["(.*)(half)(.+)(past)(.+)(six)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(to)(.+)(seven)(.*)"],
        ["(.*)(twenty)(.+)(to)(.+)(seven)(.*)"],
        ["(.*)(quarter)(.+)(to)(.+)(seven)(.*)"],
        ["(.*)(ten)(.+)(to)(.+)(seven)(.*)"],
        ["(.*)(five)(.+)(to)(.+)(seven)(.*)"],
    ],
    [
        ["(.*)(seven)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(seven)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(seven)(.*)"],
        ["(.*)(quarter)(.+)(past)(.+)(seven)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(seven)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(past)(.+)(seven)(.*)"],
        ["(.*)(half)(.+)(past)(.+)(seven)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(to)(.+)(eight)(.*)"],
        ["(.*)(twenty)(.+)(to)(.+)(eight)(.*)"],
        ["(.*)(quarter)(.+)(to)(.+)(eight)(.*)"],
        ["(.*)(ten)(.+)(to)(.+)(eight)(.*)"],
        ["(.*)(five)(.+)(to)(.+)(eight)(.*)"],
    ],
    [
        ["(.*)(eight)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(eight)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(eight)(.*)"],
        ["(.*)(quarter)(.+)(past)(.+)(eight)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(eight)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(past)(.+)(eight)(.*)"],
        ["(.*)(half)(.+)(past)(.+)(eight)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(to)(.+)(nine)(.*)"],
        ["(.*)(twenty)(.+)(to)(.+)(nine)(.*)"],
        ["(.*)(quarter)(.+)(to)(.+)(nine)(.*)"],
        ["(.*)(ten)(.+)(to)(.+)(nine)(.*)"],
        ["(.*)(five)(.+)(to)(.+)(nine)(.*)"],
    ],
    [
        ["(.*)(nine)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(nine)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(nine)(.*)"],
        ["(.*)(quarter)(.+)(past)(.+)(nine)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(nine)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(past)(.+)(nine)(.*)"],
        ["(.*)(half)(.+)(past)(.+)(nine)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(to)(.+)(ten)(.*)"],
        ["(.*)(twenty)(.+)(to)(.+)(ten)(.*)"],
        ["(.*)(quarter)(.+)(to)(.+)(ten)(.*)"],
        ["(.*)(ten)(.+)(to)(.+)(ten)(.*)"],
        ["(.*)(five)(.+)(to)(.+)(ten)(.*)"],
    ],
    [
        ["(.*)(ten)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(ten)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(ten)(.*)"],
        ["(.*)(quarter)(.+)(past)(.+)(ten)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(ten)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(past)(.+)(ten)(.*)"],
        ["(.*)(half)(.+)(past)(.+)(ten)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(to)(.+)(eleven)(.*)"],
        ["(.*)(twenty)(.+)(to)(.+)(eleven)(.*)"],
        ["(.*)(quarter)(.+)(to)(.+)(eleven)(.*)"],
        ["(.*)(ten)(.+)(to)(.+)(eleven)(.*)"],
        ["(.*)(five)(.+)(to)(.+)(eleven)(.*)"],
    ],
    [
        ["(.*)(eleven)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(eleven)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(eleven)(.*)"],
        ["(.*)(quarter)(.+)(past)(.+)(eleven)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(eleven)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(past)(.+)(eleven)(.*)"],
        ["(.*)(half)(.+)(past)(.+)(eleven)(.*)"],
        ["(.*)(twenty)(.+)(five)(.+)(to)(.+)(twelve)(.*)"],
        ["(.*)(twenty)(.+)(to)(.+)(twelve)(.*)"],
        ["(.*)(quarter)(.+)(to)(.+)(twelve)(.*)"],
        ["(.*)(ten)(.+)(to)(.+)(twelve)(.*)"],
        ["(.*)(five)(.+)(to)(.+)(twelve)(.*)"],
    ],
];
names = ["nine", "half", "six", "past", "one", "seven", "clock", "two", "three", "o", "eight", "four", "twelve", "eleven", "to", "ten", "five", "quarter", "twenty"];



function MultiRegExp(par) {
    var regex;
    if (par.source !== undefined){
        regex = par;
    } else {
        var exp = par;
        var opts = "";
        if (par.substring(0, 1) == "/") {
            var l = par.lastIndexOf("/");
            opts = par.substring(l + 1, par.length);
            exp = par.substring(1, l);
        }
        regex = new RegExp(exp, opts);
    }
    var expandSource = function (braces, indexer) {
        var ret = '';
        for (var i = 0; i < braces.length; i++) {
            if (braces[i].type == 'raw') {
                ret += '(' + braces[i].text + ')';
                indexer.next();
            } else if (braces[i].type == 'brace' && braces[i].containsCapture) {
                ret += braces[i].pre + expandSource(braces[i].children, indexer) + braces[i].post;
            } else if (braces[i].type == 'brace' && !braces[i].isCapture) {
                ret += '(' + braces[i].text.substring(braces[i].pre.length, braces[i].text.length - braces[i].post.length) + ')';
                indexer.next();
            } else if (braces[i].type == 'brace') {
                ret += braces[i].text;
                indexer.next(true);
            } else {
                ret += braces[i].text;
            }
        }
        return ret;
    }

    var captureScan = function(braces, parent) {
        var containsCapture = false;
        for (var i = 0; i < braces.length; i++) {
            captureScan(braces[i].children, braces[i]);
            braces[i].isCapture = braces[i].text.indexOf('(?:') != 0;
            if (braces[i].isCapture) {
                containsCapture = true;
            }
            if (braces[i].isCapture && braces[i].containsCapture) {
                throw "nested captures not permitted, use (?:...) where capture is not intended";
            }
        }
        if (parent) {
            parent.containsCapture = containsCapture;
        }
    }

    var fillGaps = function(braces, text) {
        var pre = /^\((\?.)?/.exec(text);
        pre = pre == null ? '' : pre[0];
        var post = /\)$/.exec(text);
        post = post == null ? '' : post[0];
        var i = 0;
        if (braces.length > 0) {
            fillGaps(braces[0].children, braces[0].text);
        }
        if (braces.length > 0 && braces[0].pos > pre.length) {
            braces.splice(0, 0, {type: 'raw', pos: pre.length, length: braces[0].pos, text: text.substring(pre.length, braces[0].pos)});
            i++;
        }
        for(i++ ;i < braces.length; i++) {
            fillGaps(braces[i].children, braces[i].text);
            if (braces[i].pos > braces[i-1].pos + braces[i-1].length) {
                braces.splice(i, 0, {type:'raw', pos: braces[i-1].pos + braces[i-1].length,
                                     length: braces[i].pos - (braces[i-1].pos + braces[i-1].length),
                                     text: text.substring(braces[i-1].pos + braces[i-1].length,
                                                          braces[i].pos)});
                i++;
            }
        }
        if (braces.length == 0)
        {
            braces.push({type:'raw', pos: pre.length, length: text.length - post.length - pre.length, text: text.substring(pre.length, text.length - post.length)});
        } else if (braces[braces.length - 1].pos + braces[braces.length - 1].length < text.length - post.length) {
            var pos = braces[braces.length - 1].pos + braces[braces.length - 1].length;
            var txt = text.substring(pos, text.length - post.length);
            braces.push({type:'raw', pos: pos, length: txt.length, text: txt});
        }
    }

    var GetBraces = function(text) {
        var ret = [];
        var shift = 0;
        do {
            var brace = GetBrace(text);
            if (brace == null) {
                break;
            } else {
                text = text.substring(brace.pos + brace.length);
                var del = brace.pos + brace.length;
                brace.pos += shift;
                shift += del;
                ret.push(brace);
            }
        } while (brace != null);
        return ret;
    }

    var GetBrace = function(text) {
        var ret = {pos: -1, length: 0, text: '', children: [], type: 'brace'};
        var openExp = /^(?:\\.|[^\)\\\(])*\(\?./;
        var pre = 3;
        var post = 1;
        var m = openExp.exec(text);
        if (m == null) {
            m = /^(?:\\.|[^\)\\\(])*\(/.exec(text);
            pre = 1;
        }
        if (m != null) {
            ret.pos = m[0].length - pre;
            ret.children = GetBraces(text.substring(m[0].length));
            for (var i = 0; i < ret.children.length; i++) {
                ret.children[i].pos += pre;
            }
            var closeExp = /^(?:\\.|[^\\\(\)])*\)/;
            var closeExpAlt = /^(?:\\.|[^\\\(\)])*\)\?/;
            var from = ret.children.length <= 0 ? ret.pos + pre :
                ret.children[ret.children.length-1].pos +
                    ret.children[ret.children.length-1].length +
                    m[0].length - pre;
            var m2 = closeExp.exec(text.substring(from));
            var m3 = closeExpAlt.exec(text.substring(from));
            if (m3 !== null && m3.length - 1 <= m2.length) {
                m2 = m3;
                post = 2;
            }
            if (m2 == null) {
                return null;
            } else {
                ret.length = m2[0].length + from - ret.pos;
                ret.text = text.substring(ret.pos, ret.pos + ret.length);
            }
        }
        if (ret.text == '()' || /^\(\?.\)$/.test(ret.text)) {
            throw 'empty braces not permitted';
        }
        if (ret.pos != -1) {
            ret.pre = ret.text.substring(0, pre);
            ret.post = ret.text.substring(ret.text.length - post, ret.text.length);
        }
        return ret.pos == -1 ? null : ret;
    }

    var fixOrs = function (braces_W_raw) {
        var orFind = /^(\\.|[^\\|])*\|/;
        for (var i = 0; i < braces_W_raw.length; i++) {
            if (braces_W_raw[i].type == 'raw') {
                var fullText = braces_W_raw[i].text;
                var m = orFind.exec(fullText);
                if (m != null) {
                    var or = { type: 'or', pos: m[0].length - 1 + braces_W_raw[i].pos, length: 1, text: '|' };
                    var raw = { type: 'raw', pos: m[0].length + braces_W_raw[i].pos,
                        length: fullText.length - m[0].length,
                        text: fullText.substring(m[0].length, fullText.length)
                    };
                    braces_W_raw[i].text = fullText.substring(0, m[0].length - 1);
                    braces_W_raw[i].length = braces_W_raw[i].text.length;
                    braces_W_raw.splice(i + 1, 0, or, raw);
                    i += 1;
                }
            } else if (braces_W_raw[i].type == 'brace') {
                fixOrs(braces_W_raw[i].children, braces_W_raw[i].text);
            }
        }
    }

    var source = regex.source;
    var braces = GetBraces(source);
    captureScan(braces);
    fillGaps(braces, source);
    fixOrs(braces);
    var indexer = {i: 1, next:
                       function (realPoint) {
                           if (realPoint) {
                               this.points.push(this.i);
                           }
                           return this.i++;
                       }, points: []};
    source = expandSource(braces, indexer);
    this.dataPoints = indexer.points;
    var options = (regex.ignoreCase ? "i" : "") + (regex.global ? "g" : "") + (regex.multiline ? "m" : "");
    this.regex = new RegExp(source, options);
    this.exec = function (text) {
        var m = this.regex.exec(text);
        if (m == null) {
            return {};
        }
        var ret = {};
        var ch = 0;
        for (var i = 1; i < m.length; i++) {
            if (m[i] !== null && m[i] !== undefined) {
                var pos = this.dataPoints.indexOf(i);
                if (pos != -1) {
                    ret[pos] = {index: ch, text: m[i]};
                }
                ch += m[i].length;
            }
        }
        for (var i = 0; i < this.dataPoints.length; i++) {
            if (ret[i] === undefined) {
                ret[i] = null;
            }
        }
        return ret;
    }
}
