select word, count(word) as count from (
select explode(split(line, " ")) as word from t1) w
group by word order by count desc;
