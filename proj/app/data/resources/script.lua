function main(...)
    local args = { ... } -- получаем все параметры в таблицу
    print(args[1])       -- выводим первый параметр
    return args[1]       -- возвращаем первый параметр (если нужно)
end

return main(...)
