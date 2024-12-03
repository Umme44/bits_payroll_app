import { Pipe, PipeTransform } from '@angular/core';
import dayjs from 'dayjs/esm';

@Pipe({
  name: 'formatDayOfTheWeek',
})
export class FormatDayOfTheWeekPipe implements PipeTransform {
  transform(day: dayjs.Dayjs | null | undefined): string {
    return day ? day.format('ddd') : '';
  }
}
