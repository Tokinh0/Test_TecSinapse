require 'net/http'
require 'net/https'
require 'json'
require 'date'
require 'benchmark'

def get_items
  uri = URI('https://eventsync.portaltecsinapse.com.br/public/recrutamento/input?email=jean-recados@outlook.com')
  res = Net::HTTP.get_response(uri)
  items = JSON.parse(res.body, {symbolize_names: true})
  items
end

def get_items_by_date(start_date, end_date, items)
  items_by_date = items.select {|e| Date.parse(e[:dia]) >= Date.parse(start_date)&& Date.parse(e[:dia]) <= Date.parse(end_date) }
  puts "items em dezembro: " + items_by_date.count.to_s
  items_by_date
end

def get_items_by_type(type_of_item, items)
  item_by_type = items.select {|e| e[:item] == type_of_item }
  item_by_type
end

def total_quantity(items)
  total = 0
  items.each do |item|
    total += item[:quantidade]
  end
  total
end

def total_value(items)
  total = 0
  items.each do |item|
    total += item[:total]
    puts "@Valor de cada item por item: " + item[:total].to_s
  end
  total
end

def send_post_resquest(answer)
  uri = URI.parse("https://eventsync.portaltecsinapse.com.br/public/recrutamento/finalizar?email=jean-recados@outlook.com")
  request = Net::HTTP::Post.new(uri)
  request.content_type = "text/plain"
  request.body = "#{answer}"
  req_options = {
    use_ssl: uri.scheme == "https",
  }
  response = Net::HTTP.start(uri.hostname, uri.port, req_options) do |http|
    http.request(request)
  end
  puts "return of the TecSinapse_API -> " + response.body
end

def order_array_of_items(items)
  type_items = ["item 1","item 2","item 3","item 4","item 5"]
  ordered_array_of_items = []
  type_items.each do |item|
      ordered_array_of_items += [get_items_by_type(item, items)]
  end
  ordered_array_of_items
end


def get_qtd_and_value(ordered_array_of_items)
  quantidades = []
  cont = 0;
  ordered_array_of_items.each do |items|
    qtd = total_quantity(items)
    quantidades += [qtd]
    puts "#quantidade Total de items por item: " + total_quantity(items).to_s
    puts "#Valor Total de items por item: " + total_value(ordered_array_of_items[cont]).to_s
    puts "-----------------------------------------------------------------------"
    cont += 1;
  end
  index = (quantidades.rindex(quantidades.max))
  valor_total = total_value(ordered_array_of_items[index])
  mais_vendido = index + 1
  "item #{mais_vendido}##{valor_total.round(2)}"
end

def run
  answer = get_qtd_and_value(order_array_of_items(get_items_by_date('2018-12-01', '2018-12-31', get_items)))
  send_post_resquest(answer)
end

run
time = Benchmark.measure do
  (1..10000).each { |i| i }
end
puts time
