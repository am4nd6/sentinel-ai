from __future__ import annotations
import argparse, json, statistics, time
from urllib.request import urlopen

def run(url: str, count: int) -> dict[str, object]:
    samples=[]
    for _ in range(count):
        start=time.perf_counter()
        with urlopen(url, timeout=10) as response:
            response.read()
            status=response.status
        samples.append((time.perf_counter()-start)*1000)
        if status != 200: raise RuntimeError(f"unexpected status {status}")
    ordered=sorted(samples)
    p95=ordered[max(0, int(len(ordered)*.95)-1)]
    return {"url":url,"requests":count,"status":"ok","min_ms":round(min(samples),2),"mean_ms":round(statistics.mean(samples),2),"p95_ms":round(p95,2),"max_ms":round(max(samples),2)}

if __name__ == "__main__":
    parser=argparse.ArgumentParser(); parser.add_argument("--url",default="http://localhost:8080/api/v1/health"); parser.add_argument("--count",type=int,default=20); args=parser.parse_args()
    print(json.dumps(run(args.url,max(1,min(args.count,1000))),indent=2))
