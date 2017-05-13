select word, count(word) as count from (
select explode(split(line, " ")) as word from test ) w group by word
order by count desc;
