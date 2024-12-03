import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAttendanceEntry, NewAttendanceEntry } from '../attendance-entry.model';

export type PartialUpdateAttendanceEntry = Partial<IAttendanceEntry> & Pick<IAttendanceEntry, 'id'>;

type RestOf<T extends IAttendanceEntry | NewAttendanceEntry> = Omit<T, 'date' | 'inTime' | 'outTime'> & {
  date?: string | null;
  inTime?: string | null;
  outTime?: string | null;
};

export type RestAttendanceEntry = RestOf<IAttendanceEntry>;

export type NewRestAttendanceEntry = RestOf<NewAttendanceEntry>;

export type PartialUpdateRestAttendanceEntry = RestOf<PartialUpdateAttendanceEntry>;

export type EntityResponseType = HttpResponse<IAttendanceEntry>;
export type EntityArrayResponseType = HttpResponse<IAttendanceEntry[]>;

@Injectable({ providedIn: 'root' })
export class AttendanceEntryService {
  /* protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-entries'); */
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/attendance-entries');

  /* api/attendance-mgt/attendance-entries */
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(attendanceEntry: NewAttendanceEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendanceEntry);
    return this.http
      .post<RestAttendanceEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(attendanceEntry: IAttendanceEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendanceEntry);
    return this.http
      .put<RestAttendanceEntry>(`${this.resourceUrl}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(attendanceEntry: PartialUpdateAttendanceEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(attendanceEntry);
    return this.http
      .patch<RestAttendanceEntry>(`${this.resourceUrl}/${this.getAttendanceEntryIdentifier(attendanceEntry)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAttendanceEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAttendanceEntry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  findExistingEntry(id: number, date: any): Observable<HttpResponse<IAttendanceEntry>> {
    return this.http
      .get<RestAttendanceEntry>(`${this.resourceUrl}/${id}/${date.format(DATE_FORMAT)}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAttendanceEntryIdentifier(attendanceEntry: Pick<IAttendanceEntry, 'id'>): number {
    return attendanceEntry.id;
  }

  compareAttendanceEntry(o1: Pick<IAttendanceEntry, 'id'> | null, o2: Pick<IAttendanceEntry, 'id'> | null): boolean {
    return o1 && o2 ? this.getAttendanceEntryIdentifier(o1) === this.getAttendanceEntryIdentifier(o2) : o1 === o2;
  }

  addAttendanceEntryToCollectionIfMissing<Type extends Pick<IAttendanceEntry, 'id'>>(
    attendanceEntryCollection: Type[],
    ...attendanceEntriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const attendanceEntries: Type[] = attendanceEntriesToCheck.filter(isPresent);
    if (attendanceEntries.length > 0) {
      const attendanceEntryCollectionIdentifiers = attendanceEntryCollection.map(
        attendanceEntryItem => this.getAttendanceEntryIdentifier(attendanceEntryItem)!
      );
      const attendanceEntriesToAdd = attendanceEntries.filter(attendanceEntryItem => {
        const attendanceEntryIdentifier = this.getAttendanceEntryIdentifier(attendanceEntryItem);
        if (attendanceEntryCollectionIdentifiers.includes(attendanceEntryIdentifier)) {
          return false;
        }
        attendanceEntryCollectionIdentifiers.push(attendanceEntryIdentifier);
        return true;
      });
      return [...attendanceEntriesToAdd, ...attendanceEntryCollection];
    }
    return attendanceEntryCollection;
  }

  protected convertDateFromClient<T extends IAttendanceEntry | NewAttendanceEntry | PartialUpdateAttendanceEntry>(
    attendanceEntry: T
  ): RestOf<T> {
    return {
      ...attendanceEntry,
      date: attendanceEntry.date?.format(DATE_FORMAT) ?? null,
      inTime: attendanceEntry.inTime?.toJSON() ?? null,
      outTime: attendanceEntry.outTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAttendanceEntry: RestAttendanceEntry): IAttendanceEntry {
    return {
      ...restAttendanceEntry,
      date: restAttendanceEntry.date ? dayjs(restAttendanceEntry.date) : undefined,
      inTime: restAttendanceEntry.inTime ? dayjs(restAttendanceEntry.inTime) : undefined,
      outTime: restAttendanceEntry.outTime ? dayjs(restAttendanceEntry.outTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAttendanceEntry>): HttpResponse<IAttendanceEntry> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAttendanceEntry[]>): HttpResponse<IAttendanceEntry[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
