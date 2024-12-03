import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IManualAttendanceEntry, NewManualAttendanceEntry } from '../manual-attendance-entry.model';

export type PartialUpdateManualAttendanceEntry = Partial<IManualAttendanceEntry> & Pick<IManualAttendanceEntry, 'id'>;

type RestOf<T extends IManualAttendanceEntry | NewManualAttendanceEntry> = Omit<T, 'date' | 'inTime' | 'outTime'> & {
  date?: string | null;
  inTime?: string | null;
  outTime?: string | null;
};

export type RestManualAttendanceEntry = RestOf<IManualAttendanceEntry>;

export type NewRestManualAttendanceEntry = RestOf<NewManualAttendanceEntry>;

export type PartialUpdateRestManualAttendanceEntry = RestOf<PartialUpdateManualAttendanceEntry>;

export type EntityResponseType = HttpResponse<IManualAttendanceEntry>;
export type EntityArrayResponseType = HttpResponse<IManualAttendanceEntry[]>;

@Injectable({ providedIn: 'root' })
export class ManualAttendanceEntryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/manual-attendance-entries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(manualAttendanceEntry: NewManualAttendanceEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manualAttendanceEntry);
    return this.http
      .post<RestManualAttendanceEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(manualAttendanceEntry: IManualAttendanceEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manualAttendanceEntry);
    return this.http
      .put<RestManualAttendanceEntry>(`${this.resourceUrl}/${this.getManualAttendanceEntryIdentifier(manualAttendanceEntry)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(manualAttendanceEntry: PartialUpdateManualAttendanceEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(manualAttendanceEntry);
    return this.http
      .patch<RestManualAttendanceEntry>(`${this.resourceUrl}/${this.getManualAttendanceEntryIdentifier(manualAttendanceEntry)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestManualAttendanceEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestManualAttendanceEntry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getManualAttendanceEntryIdentifier(manualAttendanceEntry: Pick<IManualAttendanceEntry, 'id'>): number {
    return manualAttendanceEntry.id;
  }

  compareManualAttendanceEntry(o1: Pick<IManualAttendanceEntry, 'id'> | null, o2: Pick<IManualAttendanceEntry, 'id'> | null): boolean {
    return o1 && o2 ? this.getManualAttendanceEntryIdentifier(o1) === this.getManualAttendanceEntryIdentifier(o2) : o1 === o2;
  }

  addManualAttendanceEntryToCollectionIfMissing<Type extends Pick<IManualAttendanceEntry, 'id'>>(
    manualAttendanceEntryCollection: Type[],
    ...manualAttendanceEntriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const manualAttendanceEntries: Type[] = manualAttendanceEntriesToCheck.filter(isPresent);
    if (manualAttendanceEntries.length > 0) {
      const manualAttendanceEntryCollectionIdentifiers = manualAttendanceEntryCollection.map(
        manualAttendanceEntryItem => this.getManualAttendanceEntryIdentifier(manualAttendanceEntryItem)!
      );
      const manualAttendanceEntriesToAdd = manualAttendanceEntries.filter(manualAttendanceEntryItem => {
        const manualAttendanceEntryIdentifier = this.getManualAttendanceEntryIdentifier(manualAttendanceEntryItem);
        if (manualAttendanceEntryCollectionIdentifiers.includes(manualAttendanceEntryIdentifier)) {
          return false;
        }
        manualAttendanceEntryCollectionIdentifiers.push(manualAttendanceEntryIdentifier);
        return true;
      });
      return [...manualAttendanceEntriesToAdd, ...manualAttendanceEntryCollection];
    }
    return manualAttendanceEntryCollection;
  }

  protected convertDateFromClient<T extends IManualAttendanceEntry | NewManualAttendanceEntry | PartialUpdateManualAttendanceEntry>(
    manualAttendanceEntry: T
  ): RestOf<T> {
    return {
      ...manualAttendanceEntry,
      date: manualAttendanceEntry.date?.format(DATE_FORMAT) ?? null,
      inTime: manualAttendanceEntry.inTime?.toJSON() ?? null,
      outTime: manualAttendanceEntry.outTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restManualAttendanceEntry: RestManualAttendanceEntry): IManualAttendanceEntry {
    return {
      ...restManualAttendanceEntry,
      date: restManualAttendanceEntry.date ? dayjs(restManualAttendanceEntry.date) : undefined,
      inTime: restManualAttendanceEntry.inTime ? dayjs(restManualAttendanceEntry.inTime) : undefined,
      outTime: restManualAttendanceEntry.outTime ? dayjs(restManualAttendanceEntry.outTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestManualAttendanceEntry>): HttpResponse<IManualAttendanceEntry> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestManualAttendanceEntry[]>): HttpResponse<IManualAttendanceEntry[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
