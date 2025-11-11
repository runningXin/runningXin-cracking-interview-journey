#!/bin/bash
# ==========================================
# ⚡ Concurrent YTD Fetch (macOS safe, color + 5-column)
# ==========================================

ETFS=("VOO" "QQQ" "VGT" "JEPQ" "SMH" "MAGS" "SPMO" "HDV" "XLU")
STOCKS=("TSLA" "GOOG" "META" "AAPL" "NVDA" "NFLX" "AMZN" "MSFT" "AVGO" "ASML" "QCOM" "ABNB" "UBER" "BRKB" "O" "COST" "WMT" "PEP" "KO" "CMG" "XOM" "UNH")
LEVERAGED_ETF=("SOXL" "TQQQ" "FNGU" "QLD" "SSO" "UPRO")

# 🎨 颜色定义
GREEN=$(tput setaf 2)
RED=$(tput setaf 1)
GRAY=$(tput setaf 7)
RESET=$(tput sgr0)

# --- Fetch YTD % ---
fetch_ytd_return() {
    local TICKER=$1
    local response=$(curl -s --max-time 10 -A "Mozilla/5.0" -e "https://www.cnbc.com" \
        "https://www.cnbc.com/quotes/$TICKER?qsearchterm=$TICKER")
    local ytd=$(echo "$response" | awk -F 'SplitStats-name">YTD % Change</span><span class="SplitStats-price">' '{print $2}' \
        | awk -F '<' '{print $1}' | tr -d '[:space:]')
    [[ -z "$ytd" ]] && ytd="N/A" || ytd="${ytd}%"
    echo "$TICKER $ytd"
}

# --- Print section (5 per row) ---
print_table() {
    local TITLE=$1
    shift
    local TICKERS=("$@")

    echo ""
    echo "================= $TITLE ================="
    echo "----------------------------------------------------"

    TMPFILE=$(mktemp)
    for T in "${TICKERS[@]}"; do
        fetch_ytd_return "$T" >> "$TMPFILE" &
    done
    wait

    RESULTS=()
    while read line; do
        RESULTS+=("$line")
    done < "$TMPFILE"
    rm -f "$TMPFILE"

    local total=${#RESULTS[@]}
    local i=0
    while [ $i -lt $total ]; do
        local line1=""
        local line2=""
        for j in 0 1 2 3 4; do
            local idx=$((i + j))
            if [ $idx -lt $total ]; then
                ticker=$(echo "${RESULTS[$idx]}" | awk '{print $1}')
                ytd=$(echo "${RESULTS[$idx]}" | awk '{print $2}')

                # 添加颜色
                if [[ "$ytd" == "N/A" ]]; then
                    color=$GRAY
                elif [[ "$ytd" == -* ]]; then
                    color=$RED
                else
                    color=$GREEN
                fi

                line1="$line1$(printf "%-10s" "$ticker")"
                line2="$line2$(printf "%s%-10s%s" "$color" "$ytd" "$RESET")"
            fi
        done
        echo "$line1"
        echo "$line2"
        echo "----------------------------------------------------"
        i=$((i + 5))
    done
}

# --- Main sections ---
print_table "📊 ETF PERFORMANCE" "${ETFS[@]}"
print_table "💼 STOCK PERFORMANCE" "${STOCKS[@]}"
print_table "⚡ LEVERAGED ETF PERFORMANCE" "${LEVERAGED_ETF[@]}"



