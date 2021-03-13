package world.worldStory.actualList;

import com.sun.istack.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Список с выбранным элементом основанный на списке на указателе
 */
public class LinkedActualList<T> implements List<T> {
    /**
     * текущее положение в истории мира
     */
    private final int[] actualPos;

    /**
     * Список объектов списка
     */
    @NotNull
    private List<T> objects;

    /**
     * Конструктор списка с выбранным элементом основанный на списке на указателе
     */
    public LinkedActualList() {
        this.actualPos = new int[]{0};
        objects = Collections.synchronizedList(new LinkedList<>());
    }


    /**
     * Конструктор списка с выбранным элементом основанный на списке на указателе
     *
     * @param actualList список с выбранным элементом
     * @param copy       функция копирования элементов спискам
     */
    public LinkedActualList(@NotNull LinkedActualList<T> actualList, @NotNull Function<T, T> copy) {
        objects = Collections.synchronizedList(new LinkedList<>());
        this.actualPos = new int[]{actualList.actualPos[0]};
        addAll(actualList, copy);
        this.actualPos[0] = actualList.actualPos[0];
    }

    /**
     * Конструктор списка с выбранным элементом основанный на списке на указателе
     *
     * @param actualList список с выбранным элементом
     * @param actualPos  актуальное положение
     */
    public LinkedActualList(@NotNull List<T> actualList, int actualPos) {
        objects = Collections.synchronizedList(new LinkedList<>());
        this.actualPos = new int[]{actualPos};
        addAll(actualList);
        this.actualPos[0] = actualPos;
    }

    /**
     * Размер списка
     *
     * @return размер списка
     */
    @Override
    public int size() {
        synchronized (objects) {
            return objects.size();
        }
    }

    /**
     * Получить объекты списка
     * @return объекты списка
     */
    public List<T> getObjects() {
        return objects;
    }

    /**
     * Задать объекты списка
     * @param objects объекты списка
     */
    public void setObjects(List<T> objects) {
        this.objects = objects;
    }

    /**
     * Проверка, пустой ли список
     *
     * @return пустой ли список
     */
    @Override
    public boolean isEmpty() {
        synchronized (objects) {
            return objects.isEmpty();
        }
    }

    /**
     * Проверка, содержится ли элемент в списке
     *
     * @param o элемент
     * @return содержится ли элемент в списке
     */
    @Override
    public boolean contains(Object o) {
        synchronized (objects) {
            return objects.contains(o);
        }
    }

    /**
     * Получить итератор
     *
     * @return итератор
     */
    @Override
    public Iterator<T> iterator() {
        synchronized (objects) {
            return objects.iterator();
        }
    }

    /**
     * Преобразование к массиву
     *
     * @return массив
     */
    @Override
    public Object[] toArray() {
        synchronized (objects) {
            return objects.toArray();
        }
    }

    /**
     * Преобразование к массиву
     *
     * @return массив
     */
    @Override
    public <T1> T1[] toArray(T1[] a) {
        synchronized (objects) {
            return objects.toArray(a);
        }
    }

    /**
     * Добавить элемент в список
     *
     * @param object элемент, который нужно добавить
     */
    public boolean add(T object) {
        synchronized (objects) {
            synchronized (actualPos) {
                boolean flgAdded;
                flgAdded = objects.add(object);
                incActualPos();
                //System.out.println("add " + flgAdded + " " + size());
                return flgAdded;
            }
        }
    }

    /**
     * Удалить элемент из списка
     *
     * @param o элемент
     * @return получилось ли удалить
     */
    @Override
    public boolean remove(Object o) {
        synchronized (objects) {
            synchronized (actualPos) {
                decActualPos();
                return objects.remove(o);
            }
        }
    }

    /**
     * Проверка, содеражтся ли элементы в списке
     *
     * @param c элементы
     * @return содеражтся ли элементы в списке
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        synchronized (objects) {
            return objects.containsAll(c);
        }
    }

    /**
     * Добавить элементы в список
     *
     * @param c элементы
     * @return получилось ли добавить элементы в список
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        synchronized (objects) {
            return objects.addAll(c);
        }
    }

    /**
     * Добавить элементы в список
     *
     * @param index положение
     * @param c     элементы
     * @return получилось ли добавить элементы в список
     */
    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        synchronized (objects) {
            return objects.addAll(index, c);
        }
    }

    /**
     * Удалить элементы из списока
     *
     * @param c элементы
     * @return получилось ли удалить элементы в список
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        synchronized (objects) {
            synchronized (actualPos) {
                boolean flgRemoved = objects.removeAll(c);
                setActualPosToLast();
                return flgRemoved;
            }
        }
    }

    /**
     * Удалить элементы из списока
     *
     * @param c элементы
     * @return получилось ли удалить элементы в список
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        synchronized (objects) {
            boolean flgRetain = objects.retainAll(c);
            setActualPosToLast();
            return flgRetain;
        }
    }


    /**
     * Очистить список и задать начальный объект
     *
     * @param object объект
     */
    public void clear(T object) {
        synchronized (objects) {
            synchronized (actualPos) {
                clear();
                add(object);
            }
        }
    }

    /**
     * Добавить все элементы из другого списка
     *
     * @param actualList список-образец
     * @param copy       функция копирования элементов списка
     */
    public void addAll(List<T> actualList, Function<T, T> copy) {
        for (T elem : actualList)
            this.add(copy.apply(elem));
    }

    /**
     * Получить текущий объект списка
     *
     * @return текущий объект списка
     */
    public T getActual() {
        synchronized (actualPos) {
            return get(actualPos[0]);
        }
    }

    /**
     * Очистить список
     */
    public void clear() {
        synchronized (objects) {
            synchronized (actualPos) {
                objects.clear();
                actualPos[0] = 0;
            }
        }
    }

    /**
     * Получить элемент по номеру
     *
     * @param index индекс
     * @return элемент
     */
    @Override
    public synchronized T get(int index) {
        synchronized (objects) {
            return objects.get(index);
        }
    }

    /**
     * Получить последний элемент
     *
     * @return элемент
     */
    public T getLast() {
        synchronized (objects) {
            if (objects.isEmpty())
                throw new AssertionError("list is empty");
            return objects.get(objects.size() - 1);
        }
    }

    /**
     * Задать элемент по номеру
     *
     * @param index   индекс
     * @param element элемент
     * @return элемент
     */
    @Override
    public T set(int index, T element) {
        synchronized (objects) {
            return objects.set(index, element);
        }
    }

    /**
     * Добавить элемент
     *
     * @param index   индекс
     * @param element элемент
     */
    @Override
    public void add(int index, T element) {
        synchronized (objects) {
            objects.add(index, element);
        }
    }

    /**
     * Удалить элемент по номеру
     *
     * @param index индекс
     * @return элемент
     */
    @Override
    public T remove(int index) {
        synchronized (objects) {
            synchronized (actualPos) {
                decActualPos();
                return objects.remove(index);
            }
        }
    }

    /**
     * Получить индекс элемента по значению
     *
     * @param o значение
     * @return индекс
     */
    @Override
    public int indexOf(Object o) {
        synchronized (objects) {
            return objects.indexOf(o);
        }
    }

    /**
     * Получить последний индекс элемента по значению
     *
     * @param o значение
     * @return индекс
     */
    @Override
    public int lastIndexOf(Object o) {
        synchronized (objects) {
            return objects.lastIndexOf(o);
        }
    }

    /**
     * Получить листератор
     *
     * @return листератор
     */
    @Override
    public ListIterator<T> listIterator() {
        synchronized (objects) {
            return objects.listIterator();
        }
    }

    /**
     * Получить листератор по индексу
     *
     * @param index индекс
     * @return листератор
     */
    @Override
    public ListIterator<T> listIterator(int index) {
        synchronized (objects) {
            return objects.listIterator(index);
        }
    }

    /**
     * Получить подсписок
     *
     * @param fromIndex начальный индекс
     * @param toIndex   конечный индекс
     * @return подсписок
     */
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        synchronized (objects) {
            return new LinkedActualList<>(objects.subList(fromIndex, toIndex), toIndex - fromIndex);
        }
    }

    /**
     * Оставить в списке только подходящие элементы
     *
     * @param checkState предикат проверки элементов
     */
    public void remove(Predicate<T> checkState) {
        synchronized (objects) {
            synchronized (actualPos) {
                objects.removeIf(checkState);
                // убираем из истории все состояния, полученные из главного потока
                setActualPosToLast();
            }
        }
    }

    /**
     * Сделать актуальным последний объект
     */
    public void setActualPosToLast() {
        synchronized (actualPos) {
            actualPos[0] = size() - 1;
            if (actualPos[0] < 0)
                actualPos[0] = 0;
        }
    }


    /**
     * Удалить первый объект из списка
     *
     * @return первый обхект
     */
    public T popFirst() {
        if (size() > 0) {
            synchronized (actualPos) {
                T res = this.remove(0);
                // задаём текущее состояние как последнее в истории
                actualPos[0] = this.size() - 1;
                if (actualPos[0] < 0) actualPos[0] = 0;
                return res;
            }
        }
        throw new AssertionError("list is empty");
    }

    /**
     * Обрезать список(если выбранный объект не последний)
     *
     * @return обрезался ли список
     */
    public synchronized boolean truncByActualPos() {
        synchronized (actualPos) {
            //обрезать все кадры истории мира, если после паузы старт произошёл не с последнего карта
            if (actualPos[0] < this.size() - 1) {
                synchronized (objects) {
                    objects.subList(actualPos[0], objects.size()).clear();
                    actualPos[0] = objects.size() - 1;
                    if (actualPos[0] < 0)
                        actualPos[0] = 0;
                }
                return true;
            }
            return false;
        }
    }

    /**
     * Удалить последний объект списка
     *
     * @return последний объект списка
     */
    public T popLast() {
        if (isEmpty())
            throw new AssertionError("list size is one or less");
        synchronized (objects) {
            synchronized (actualPos) {
                T result = objects.remove(size() - 1);
                if (actualPos[0] > size() - 1)
                    // меняем номер состояния на последний допустимый
                    actualPos[0] = size() - 1;

                if (actualPos[0] < 0)
                    actualPos[0] = 0;
                return result;
            }
        }
    }


    /**
     * Удалить последний объект списка
     */
    public void removeLast() {
        if (size() <= 1)
            return;
        synchronized (objects) {
            synchronized (actualPos) {
                objects.remove(size() - 1);
                if (actualPos[0] > size() - 1)
                    // меняем номер состояния на последний допустимый
                    actualPos[0] = size() - 1;

                if (actualPos[0] < 0)
                    actualPos[0] = 0;
            }
        }
    }


    /**
     * Поменять выбранный объект
     *
     * @param object объект
     */
    public void setToActual(T object) {
        synchronized (actualPos) {
            set(actualPos[0], object);
        }
    }

    /**
     * Получить положение выбранного объекта
     *
     * @return положение выбранного объекта
     */
    public int getActualPos() {
        synchronized (actualPos) {
            return actualPos[0];
        }
    }

    /**
     * Задать выбранный объект
     *
     * @param newPos новый номер выбранного объекта
     * @return сколько объектов не хватает, чтобы задать новый выбранный объект
     */
    public int setActualPos(int newPos) {
        synchronized (actualPos) {
            int delta = newPos - (size() - 1);
            if (delta < 0)
                delta = 0;
            actualPos[0] = newPos;
            if (actualPos[0] > size() - 1) {
                actualPos[0] = size() - 1;
            }
            if (actualPos[0] < 0)
                actualPos[0] = 0;
            return delta;
        }
    }

    /**
     * Выбрать следующий объект
     *
     * @return получилось ли выбрать следующий объект
     */
    public boolean incActualPos() {
        synchronized (actualPos) {
            actualPos[0]++;
            if (actualPos[0] >= size()) {
                actualPos[0] = size() - 1;
                return false;
            }
            return true;
        }
    }

    /**
     * Выбрать предыдущий объект
     *
     * @return получилось ли выбрать предыдущий объект
     */
    public boolean decActualPos() {
        synchronized (actualPos) {
            actualPos[0]--;
            if (actualPos[0] < 0) {
                actualPos[0] = 0;
                return false;
            }
            return true;
        }
    }

    /**
     * Перейти к концу истории
     */
    public void moveToStart() {
        System.out.println("move to start");
        synchronized (actualPos) {
            actualPos[0] = 0;
            System.out.println(actualPos[0]);
        }
    }

    /**
     * Перейти к концу истории
     */
    public void moveToEnd() {
        synchronized (objects) {
            synchronized (actualPos) {
                actualPos[0] = objects.size() - 1;
                if (actualPos[0] < 0)
                    actualPos[0] = 0;
                System.out.println(actualPos[0]);
            }
        }
    }

    /**
     * Дублировать последний объект
     *
     * @param copy функция копирования
     */
    public void duplicateLast(Function<T, T> copy) {
        add(copy.apply(get(size() - 1)));
    }

    @Override
    public String toString() {
        synchronized (objects) {
            return "LinkedActualList{" +
                    "objects=" + objects +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedActualList<?> that = (LinkedActualList<?>) o;
        return actualPos == that.actualPos &&
                Objects.equals(objects, that.objects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actualPos, objects);
    }


}
