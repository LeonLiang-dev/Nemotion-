export const parseExamDateTime = (value?: string): Date | null => {
  if (!value) return null;

  const trimmed = value.trim();
  if (/^\d{14}$/.test(trimmed)) {
    return new Date(
      Number(trimmed.slice(0, 4)),
      Number(trimmed.slice(4, 6)) - 1,
      Number(trimmed.slice(6, 8)),
      Number(trimmed.slice(8, 10)),
      Number(trimmed.slice(10, 12)),
      Number(trimmed.slice(12, 14)),
    );
  }

  if (/^\d{12}$/.test(trimmed)) {
    return new Date(
      Number(trimmed.slice(0, 4)),
      Number(trimmed.slice(4, 6)) - 1,
      Number(trimmed.slice(6, 8)),
      Number(trimmed.slice(8, 10)),
      Number(trimmed.slice(10, 12)),
      0,
    );
  }

  const normalized = trimmed.replace('T', ' ').slice(0, 19).replace(' ', 'T');
  const date = new Date(normalized);
  return Number.isNaN(date.getTime()) ? null : date;
};

export const formatExamDateTime = (value?: string) => {
  const date = parseExamDateTime(value);
  if (!date) return '-';

  const pad = (num: number) => num.toString().padStart(2, '0');
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}`;
};

export const getRequestErrorMessage = (error: any, fallback: string) => (
  error?.data?.message || error?.response?.data?.message || error?.message || fallback
);
