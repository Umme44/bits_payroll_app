import dayjs from 'dayjs/esm';

export const calculateDurationOnDays = (startDate: any, endDate: any, willIncludeLastDay = true): number => {
  const start = dayjs(startDate);
  const end = dayjs(endDate);
  const duration = dayjs.duration(end.diff(start));
  if (willIncludeLastDay) return duration.asDays() + 1;
  else return duration.asDays();
};
