"use strict";

var timeCounter = 0;
var schResults = document.getElementById("schResults");
var schInput = document.getElementById("schInput");
//var watchFace = "elevenntsix|sevenfourty|twelvehotwo|eightoneten|threertnine|fivefifteen|twentypastl|fourthytenv|fiftythirty|clockfourty|gtwofiveten";
var watchFace = "seleventhree|twelvetwotye|sponeuaeight|nineefhfivek|sixfourtenty|sevenfitenee|fortyiclocky|thfpastwenty|vfiftyhretwo|twelvegeight|ftqthirtyock|fiveffifteen";
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
        ["(.*)(one)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(one)(.*)", "(.*)(one)(.+)(o)(.+)(five)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(one)(.*)", "(.*)(one)(.+)(ten)(.*)"],
        ["(.*)(fifteen)(.+)(past)(.+)(one)(.*)", "(.*)(one)(.+)(fifteen)(.*)", "(.*)(quarter)(.+)(past)(.+)(one)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(one)(.*)", "(.*)(one)(.+)(twenty)(.*)"],
        ["(.*)(twenty)(.*)(five)(.+)(past)(.+)(one)(.*)", "(.*)(one)(.+)(twenty)(.*)(five)(.*)"],
        ["(.*)(thirty)(.+)(past)(.+)(one)(.*)", "(.*)(one)(.+)(thirty)(.*)", "(.*)(half)(.+)(past)(.+)(one)(.*)"],
        ["(.*)(thirty)(.*)(five)(.+)(past)(.+)(one)(.*)", "(.*)(one)(.+)(thirty)(.*)(five)(.*)"],
        ["(.*)(forty)(.+)(past)(.+)(one)(.*)", "(.*)(one)(.+)(forty)(.*)"],
        ["(.*)(forty)(.*)(five)(.+)(past)(.+)(one)(.*)", "(.*)(one)(.+)(forty)(.*)(five)(.*)", "(.*)(quarter)(.+)(to)(.+)(two)(.*)"],
        ["(.*)(fifty)(.+)(past)(.+)(one)(.*)", "(.*)(one)(.+)(fifty)(.*)"],
        ["(.*)(fifty)(.*)(five)(.+)(past)(.+)(one)(.*)", "(.*)(one)(.+)(fifty)(.*)(five)(.*)"],
    ],
    [
        ["(.*)(two)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(two)(.*)", "(.*)(two)(.+)(o)(.+)(five)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(two)(.*)", "(.*)(two)(.+)(ten)(.*)"],
        ["(.*)(fifteen)(.+)(past)(.+)(two)(.*)", "(.*)(two)(.+)(fifteen)(.*)", "(.*)(quarter)(.+)(past)(.+)(two)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(two)(.*)", "(.*)(two)(.+)(twenty)(.*)"],
        ["(.*)(twenty)(.*)(five)(.+)(past)(.+)(two)(.*)", "(.*)(two)(.+)(twenty)(.*)(five)(.*)"],
        ["(.*)(thirty)(.+)(past)(.+)(two)(.*)", "(.*)(two)(.+)(thirty)(.*)", "(.*)(half)(.+)(past)(.+)(two)(.*)"],
        ["(.*)(thirty)(.*)(five)(.+)(past)(.+)(two)(.*)", "(.*)(two)(.+)(thirty)(.*)(five)(.*)"],
        ["(.*)(forty)(.+)(past)(.+)(two)(.*)", "(.*)(two)(.+)(forty)(.*)"],
        ["(.*)(forty)(.*)(five)(.+)(past)(.+)(two)(.*)", "(.*)(two)(.+)(forty)(.*)(five)(.*)", "(.*)(quarter)(.+)(to)(.+)(three)(.*)"],
        ["(.*)(fifty)(.+)(past)(.+)(two)(.*)", "(.*)(two)(.+)(fifty)(.*)"],
        ["(.*)(fifty)(.*)(five)(.+)(past)(.+)(two)(.*)", "(.*)(two)(.+)(fifty)(.*)(five)(.*)"],
    ],
    [
        ["(.*)(three)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(three)(.*)", "(.*)(three)(.+)(o)(.+)(five)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(three)(.*)", "(.*)(three)(.+)(ten)(.*)"],
        ["(.*)(fifteen)(.+)(past)(.+)(three)(.*)", "(.*)(three)(.+)(fifteen)(.*)", "(.*)(quarter)(.+)(past)(.+)(three)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(three)(.*)", "(.*)(three)(.+)(twenty)(.*)"],
        ["(.*)(twenty)(.*)(five)(.+)(past)(.+)(three)(.*)", "(.*)(three)(.+)(twenty)(.*)(five)(.*)"],
        ["(.*)(thirty)(.+)(past)(.+)(three)(.*)", "(.*)(three)(.+)(thirty)(.*)", "(.*)(half)(.+)(past)(.+)(three)(.*)"],
        ["(.*)(thirty)(.*)(five)(.+)(past)(.+)(three)(.*)", "(.*)(three)(.+)(thirty)(.*)(five)(.*)"],
        ["(.*)(forty)(.+)(past)(.+)(three)(.*)", "(.*)(three)(.+)(forty)(.*)"],
        ["(.*)(forty)(.*)(five)(.+)(past)(.+)(three)(.*)", "(.*)(three)(.+)(forty)(.*)(five)(.*)", "(.*)(quarter)(.+)(to)(.+)(four)(.*)"],
        ["(.*)(fifty)(.+)(past)(.+)(three)(.*)", "(.*)(three)(.+)(fifty)(.*)"],
        ["(.*)(fifty)(.*)(five)(.+)(past)(.+)(three)(.*)", "(.*)(three)(.+)(fifty)(.*)(five)(.*)"],
    ],
    [
        ["(.*)(four)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(four)(.*)", "(.*)(four)(.+)(o)(.+)(five)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(four)(.*)", "(.*)(four)(.+)(ten)(.*)"],
        ["(.*)(fifteen)(.+)(past)(.+)(four)(.*)", "(.*)(four)(.+)(fifteen)(.*)", "(.*)(quarter)(.+)(past)(.+)(four)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(four)(.*)", "(.*)(four)(.+)(twenty)(.*)"],
        ["(.*)(twenty)(.*)(five)(.+)(past)(.+)(four)(.*)", "(.*)(four)(.+)(twenty)(.*)(five)(.*)"],
        ["(.*)(thirty)(.+)(past)(.+)(four)(.*)", "(.*)(four)(.+)(thirty)(.*)", "(.*)(half)(.+)(past)(.+)(four)(.*)"],
        ["(.*)(thirty)(.*)(five)(.+)(past)(.+)(four)(.*)", "(.*)(four)(.+)(thirty)(.*)(five)(.*)"],
        ["(.*)(forty)(.+)(past)(.+)(four)(.*)", "(.*)(four)(.+)(forty)(.*)"],
        ["(.*)(forty)(.*)(five)(.+)(past)(.+)(four)(.*)", "(.*)(four)(.+)(forty)(.*)(five)(.*)", "(.*)(quarter)(.+)(to)(.+)(five)(.*)"],
        ["(.*)(fifty)(.+)(past)(.+)(four)(.*)", "(.*)(four)(.+)(fifty)(.*)"],
        ["(.*)(fifty)(.*)(five)(.+)(past)(.+)(four)(.*)", "(.*)(four)(.+)(fifty)(.*)(five)(.*)"],
    ],
    [
        ["(.*)(five)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(five)(.*)", "(.*)(five)(.+)(o)(.+)(five)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(five)(.*)", "(.*)(five)(.+)(ten)(.*)"],
        ["(.*)(fifteen)(.+)(past)(.+)(five)(.*)", "(.*)(five)(.+)(fifteen)(.*)", "(.*)(quarter)(.+)(past)(.+)(five)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(five)(.*)", "(.*)(five)(.+)(twenty)(.*)"],
        ["(.*)(twenty)(.*)(five)(.+)(past)(.+)(five)(.*)", "(.*)(five)(.+)(twenty)(.*)(five)(.*)"],
        ["(.*)(thirty)(.+)(past)(.+)(five)(.*)", "(.*)(five)(.+)(thirty)(.*)", "(.*)(half)(.+)(past)(.+)(five)(.*)"],
        ["(.*)(thirty)(.*)(five)(.+)(past)(.+)(five)(.*)", "(.*)(five)(.+)(thirty)(.*)(five)(.*)"],
        ["(.*)(forty)(.+)(past)(.+)(five)(.*)", "(.*)(five)(.+)(forty)(.*)"],
        ["(.*)(forty)(.*)(five)(.+)(past)(.+)(five)(.*)", "(.*)(five)(.+)(forty)(.*)(five)(.*)", "(.*)(quarter)(.+)(to)(.+)(six)(.*)"],
        ["(.*)(fifty)(.+)(past)(.+)(five)(.*)", "(.*)(five)(.+)(fifty)(.*)"],
        ["(.*)(fifty)(.*)(five)(.+)(past)(.+)(five)(.*)", "(.*)(five)(.+)(fifty)(.*)(five)(.*)"],
    ],
    [
        ["(.*)(six)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(six)(.*)", "(.*)(six)(.+)(o)(.+)(five)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(six)(.*)", "(.*)(six)(.+)(ten)(.*)"],
        ["(.*)(fifteen)(.+)(past)(.+)(six)(.*)", "(.*)(six)(.+)(fifteen)(.*)", "(.*)(quarter)(.+)(past)(.+)(six)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(six)(.*)", "(.*)(six)(.+)(twenty)(.*)"],
        ["(.*)(twenty)(.*)(five)(.+)(past)(.+)(six)(.*)", "(.*)(six)(.+)(twenty)(.*)(five)(.*)"],
        ["(.*)(thirty)(.+)(past)(.+)(six)(.*)", "(.*)(six)(.+)(thirty)(.*)", "(.*)(half)(.+)(past)(.+)(six)(.*)"],
        ["(.*)(thirty)(.*)(five)(.+)(past)(.+)(six)(.*)", "(.*)(six)(.+)(thirty)(.*)(five)(.*)"],
        ["(.*)(forty)(.+)(past)(.+)(six)(.*)", "(.*)(six)(.+)(forty)(.*)"],
        ["(.*)(forty)(.*)(five)(.+)(past)(.+)(six)(.*)", "(.*)(six)(.+)(forty)(.*)(five)(.*)", "(.*)(quarter)(.+)(to)(.+)(seven)(.*)"],
        ["(.*)(fifty)(.+)(past)(.+)(six)(.*)", "(.*)(six)(.+)(fifty)(.*)"],
        ["(.*)(fifty)(.*)(five)(.+)(past)(.+)(six)(.*)", "(.*)(six)(.+)(fifty)(.*)(five)(.*)"],
    ],
    [
        ["(.*)(seven)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(seven)(.*)", "(.*)(seven)(.+)(o)(.+)(five)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(seven)(.*)", "(.*)(seven)(.+)(ten)(.*)"],
        ["(.*)(fifteen)(.+)(past)(.+)(seven)(.*)", "(.*)(seven)(.+)(fifteen)(.*)", "(.*)(quarter)(.+)(past)(.+)(seven)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(seven)(.*)", "(.*)(seven)(.+)(twenty)(.*)"],
        ["(.*)(twenty)(.*)(five)(.+)(past)(.+)(seven)(.*)", "(.*)(seven)(.+)(twenty)(.*)(five)(.*)"],
        ["(.*)(thirty)(.+)(past)(.+)(seven)(.*)", "(.*)(seven)(.+)(thirty)(.*)", "(.*)(half)(.+)(past)(.+)(seven)(.*)"],
        ["(.*)(thirty)(.*)(five)(.+)(past)(.+)(seven)(.*)", "(.*)(seven)(.+)(thirty)(.*)(five)(.*)"],
        ["(.*)(forty)(.+)(past)(.+)(seven)(.*)", "(.*)(seven)(.+)(forty)(.*)"],
        ["(.*)(forty)(.*)(five)(.+)(past)(.+)(seven)(.*)", "(.*)(seven)(.+)(forty)(.*)(five)(.*)", "(.*)(quarter)(.+)(to)(.+)(eight)(.*)"],
        ["(.*)(fifty)(.+)(past)(.+)(seven)(.*)", "(.*)(seven)(.+)(fifty)(.*)"],
        ["(.*)(fifty)(.*)(five)(.+)(past)(.+)(seven)(.*)", "(.*)(seven)(.+)(fifty)(.*)(five)(.*)"],
    ],
    [
        ["(.*)(eight)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(eight)(.*)", "(.*)(eight)(.+)(o)(.+)(five)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(eight)(.*)", "(.*)(eight)(.+)(ten)(.*)"],
        ["(.*)(fifteen)(.+)(past)(.+)(eight)(.*)", "(.*)(eight)(.+)(fifteen)(.*)", "(.*)(quarter)(.+)(past)(.+)(eight)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(eight)(.*)", "(.*)(eight)(.+)(twenty)(.*)"],
        ["(.*)(twenty)(.*)(five)(.+)(past)(.+)(eight)(.*)", "(.*)(eight)(.+)(twenty)(.*)(five)(.*)"],
        ["(.*)(thirty)(.+)(past)(.+)(eight)(.*)", "(.*)(eight)(.+)(thirty)(.*)", "(.*)(half)(.+)(past)(.+)(eight)(.*)"],
        ["(.*)(thirty)(.*)(five)(.+)(past)(.+)(eight)(.*)", "(.*)(eight)(.+)(thirty)(.*)(five)(.*)"],
        ["(.*)(forty)(.+)(past)(.+)(eight)(.*)", "(.*)(eight)(.+)(forty)(.*)"],
        ["(.*)(forty)(.*)(five)(.+)(past)(.+)(eight)(.*)", "(.*)(eight)(.+)(forty)(.*)(five)(.*)", "(.*)(quarter)(.+)(to)(.+)(nine)(.*)"],
        ["(.*)(fifty)(.+)(past)(.+)(eight)(.*)", "(.*)(eight)(.+)(fifty)(.*)"],
        ["(.*)(fifty)(.*)(five)(.+)(past)(.+)(eight)(.*)", "(.*)(eight)(.+)(fifty)(.*)(five)(.*)"],
    ],
    [
        ["(.*)(nine)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(nine)(.*)", "(.*)(nine)(.+)(o)(.+)(five)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(nine)(.*)", "(.*)(nine)(.+)(ten)(.*)"],
        ["(.*)(fifteen)(.+)(past)(.+)(nine)(.*)", "(.*)(nine)(.+)(fifteen)(.*)", "(.*)(quarter)(.+)(past)(.+)(nine)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(nine)(.*)", "(.*)(nine)(.+)(twenty)(.*)"],
        ["(.*)(twenty)(.*)(five)(.+)(past)(.+)(nine)(.*)", "(.*)(nine)(.+)(twenty)(.*)(five)(.*)"],
        ["(.*)(thirty)(.+)(past)(.+)(nine)(.*)", "(.*)(nine)(.+)(thirty)(.*)", "(.*)(half)(.+)(past)(.+)(nine)(.*)"],
        ["(.*)(thirty)(.*)(five)(.+)(past)(.+)(nine)(.*)", "(.*)(nine)(.+)(thirty)(.*)(five)(.*)"],
        ["(.*)(forty)(.+)(past)(.+)(nine)(.*)", "(.*)(nine)(.+)(forty)(.*)"],
        ["(.*)(forty)(.*)(five)(.+)(past)(.+)(nine)(.*)", "(.*)(nine)(.+)(forty)(.*)(five)(.*)", "(.*)(quarter)(.+)(to)(.+)(ten)(.*)"],
        ["(.*)(fifty)(.+)(past)(.+)(nine)(.*)", "(.*)(nine)(.+)(fifty)(.*)"],
        ["(.*)(fifty)(.*)(five)(.+)(past)(.+)(nine)(.*)", "(.*)(nine)(.+)(fifty)(.*)(five)(.*)"],
    ],
    [
        ["(.*)(ten)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(ten)(.*)", "(.*)(ten)(.+)(o)(.+)(five)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(ten)(.*)", "(.*)(ten)(.+)(ten)(.*)"],
        ["(.*)(fifteen)(.+)(past)(.+)(ten)(.*)", "(.*)(ten)(.+)(fifteen)(.*)", "(.*)(quarter)(.+)(past)(.+)(ten)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(ten)(.*)", "(.*)(ten)(.+)(twenty)(.*)"],
        ["(.*)(twenty)(.*)(five)(.+)(past)(.+)(ten)(.*)", "(.*)(ten)(.+)(twenty)(.*)(five)(.*)"],
        ["(.*)(thirty)(.+)(past)(.+)(ten)(.*)", "(.*)(ten)(.+)(thirty)(.*)", "(.*)(half)(.+)(past)(.+)(ten)(.*)"],
        ["(.*)(thirty)(.*)(five)(.+)(past)(.+)(ten)(.*)", "(.*)(ten)(.+)(thirty)(.*)(five)(.*)"],
        ["(.*)(forty)(.+)(past)(.+)(ten)(.*)", "(.*)(ten)(.+)(forty)(.*)"],
        ["(.*)(forty)(.*)(five)(.+)(past)(.+)(ten)(.*)", "(.*)(ten)(.+)(forty)(.*)(five)(.*)", "(.*)(quarter)(.+)(to)(.+)(eleven)(.*)"],
        ["(.*)(fifty)(.+)(past)(.+)(ten)(.*)", "(.*)(ten)(.+)(fifty)(.*)"],
        ["(.*)(fifty)(.*)(five)(.+)(past)(.+)(ten)(.*)", "(.*)(ten)(.+)(fifty)(.*)(five)(.*)"],
    ],
    [
        ["(.*)(eleven)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(eleven)(.*)", "(.*)(eleven)(.+)(o)(.+)(five)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(eleven)(.*)", "(.*)(eleven)(.+)(ten)(.*)"],
        ["(.*)(fifteen)(.+)(past)(.+)(eleven)(.*)", "(.*)(eleven)(.+)(fifteen)(.*)", "(.*)(quarter)(.+)(past)(.+)(eleven)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(eleven)(.*)", "(.*)(eleven)(.+)(twenty)(.*)"],
        ["(.*)(twenty)(.*)(five)(.+)(past)(.+)(eleven)(.*)", "(.*)(eleven)(.+)(twenty)(.*)(five)(.*)"],
        ["(.*)(thirty)(.+)(past)(.+)(eleven)(.*)", "(.*)(eleven)(.+)(thirty)(.*)", "(.*)(half)(.+)(past)(.+)(eleven)(.*)"],
        ["(.*)(thirty)(.*)(five)(.+)(past)(.+)(eleven)(.*)", "(.*)(eleven)(.+)(thirty)(.*)(five)(.*)"],
        ["(.*)(forty)(.+)(past)(.+)(eleven)(.*)", "(.*)(eleven)(.+)(forty)(.*)"],
        ["(.*)(forty)(.*)(five)(.+)(past)(.+)(eleven)(.*)", "(.*)(eleven)(.+)(forty)(.*)(five)(.*)", "(.*)(quarter)(.+)(to)(.+)(twelve)(.*)"],
        ["(.*)(fifty)(.+)(past)(.+)(eleven)(.*)", "(.*)(eleven)(.+)(fifty)(.*)"],
        ["(.*)(fifty)(.*)(five)(.+)(past)(.+)(eleven)(.*)", "(.*)(eleven)(.+)(fifty)(.*)(five)(.*)"],
    ],
    [
        ["(.*)(twelve)(.+)(o)(.+)(clock)(.*)"],
        ["(.*)(five)(.+)(past)(.+)(twelve)(.*)", "(.*)(twelve)(.+)(o)(.+)(five)(.*)"],
        ["(.*)(ten)(.+)(past)(.+)(twelve)(.*)", "(.*)(twelve)(.+)(ten)(.*)"],
        ["(.*)(fifteen)(.+)(past)(.+)(twelve)(.*)", "(.*)(twelve)(.+)(fifteen)(.*)", "(.*)(quarter)(.+)(past)(.+)(twelve)(.*)"],
        ["(.*)(twenty)(.+)(past)(.+)(twelve)(.*)", "(.*)(twelve)(.+)(twenty)(.*)"],
        ["(.*)(twenty)(.*)(five)(.+)(past)(.+)(twelve)(.*)", "(.*)(twelve)(.+)(twenty)(.*)(five)(.*)"],
        ["(.*)(thirty)(.+)(past)(.+)(twelve)(.*)", "(.*)(twelve)(.+)(thirty)(.*)", "(.*)(half)(.+)(past)(.+)(twelve)(.*)"],
        ["(.*)(thirty)(.*)(five)(.+)(past)(.+)(twelve)(.*)", "(.*)(twelve)(.+)(thirty)(.*)(five)(.*)"],
        ["(.*)(forty)(.+)(past)(.+)(twelve)(.*)", "(.*)(twelve)(.+)(forty)(.*)"],
        ["(.*)(forty)(.*)(five)(.+)(past)(.+)(twelve)(.*)", "(.*)(twelve)(.+)(forty)(.*)(five)(.*)", "(.*)(quarter)(.+)(to)(.+)(one)(.*)"],
        ["(.*)(fifty)(.+)(past)(.+)(twelve)(.*)", "(.*)(twelve)(.+)(fifty)(.*)"],
        ["(.*)(fifty)(.*)(five)(.+)(past)(.+)(twelve)(.*)", "(.*)(twelve)(.+)(fifty)(.*)(five)(.*)"],
    ],
];
names = ["fifteen", "nine", "half", "six", "past", "fifty", "one", "seven", "clock", "two", "three", "o", "eight", "four", "twelve", "eleven", "to", "ten", "five", "quarter", "twenty", "thirty", "forty"];




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