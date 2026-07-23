import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import type { Question } from '../types';

export const usePaperCreateStore = defineStore('paperCreate', () => {
  const form = ref({
    title: '',
    description: '',
    duration: 90,
  });

  const selectedChapters = ref<string[]>([]);

  const questionTypes = ref([
    { type: 1, name: '单选题', count: 0, score: 2, maxCount: 100 },
    { type: 2, name: '多选题', count: 0, score: 3, maxCount: 50 },
    { type: 3, name: '判断题', count: 0, score: 1, maxCount: 100 },
    { type: 4, name: '填空题', count: 0, score: 2, maxCount: 50 },
    { type: 5, name: '简答题', count: 0, score: 10, maxCount: 10 },
    { type: 6, name: '编程题', count: 0, score: 20, maxCount: 5 },
  ]);

  const selectedQuestions = ref<Question[]>([]);

  const totalScore = computed(() => {
    return selectedQuestions.value.reduce((sum, q) => sum + q.score, 0);
  });

  const totalQuestions = computed(() => {
    return selectedQuestions.value.length;
  });

  function addQuestions(questions: Question[]) {
    questions.forEach(q => {
      if (!selectedQuestions.value.find(sq => sq.id === q.id)) {
        selectedQuestions.value.push(q);
      }
    });
  }

  function removeQuestion(id: number) {
    const index = selectedQuestions.value.findIndex(q => q.id === id);
    if (index > -1) {
      selectedQuestions.value.splice(index, 1);
    }
  }

  function clearAll() {
    selectedQuestions.value = [];
  }

  function reset() {
    form.value = { title: '', description: '', duration: 90 };
    selectedChapters.value = [];
    questionTypes.value = [
      { type: 1, name: '单选题', count: 0, score: 2, maxCount: 100 },
      { type: 2, name: '多选题', count: 0, score: 3, maxCount: 50 },
      { type: 3, name: '判断题', count: 0, score: 1, maxCount: 100 },
      { type: 4, name: '填空题', count: 0, score: 2, maxCount: 50 },
      { type: 5, name: '简答题', count: 0, score: 10, maxCount: 10 },
      { type: 6, name: '编程题', count: 0, score: 20, maxCount: 5 },
    ];
    selectedQuestions.value = [];
  }

  return {
    form,
    selectedChapters,
    questionTypes,
    selectedQuestions,
    totalScore,
    totalQuestions,
    addQuestions,
    removeQuestion,
    clearAll,
    reset,
  };
});
