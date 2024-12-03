import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMovementEntry, NewMovementEntry } from '../movement-entry.model';

export type PartialUpdateMovementEntry = Partial<IMovementEntry> & Pick<IMovementEntry, 'id'>;

type RestOf<T extends IMovementEntry | NewMovementEntry> = Omit<
  T,
  'startDate' | 'startTime' | 'endDate' | 'endTime' | 'createdAt' | 'updatedAt' | 'sanctionAt'
> & {
  startDate?: string | null;
  startTime?: string | null;
  endDate?: string | null;
  endTime?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
  sanctionAt?: string | null;
};

export type RestMovementEntry = RestOf<IMovementEntry>;

export type NewRestMovementEntry = RestOf<NewMovementEntry>;

export type PartialUpdateRestMovementEntry = RestOf<PartialUpdateMovementEntry>;

export type EntityResponseType = HttpResponse<IMovementEntry>;
export type EntityArrayResponseType = HttpResponse<IMovementEntry[]>;

@Injectable({ providedIn: 'root' })
export class MovementEntryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/attendance-mgt/movement-entries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(movementEntry: NewMovementEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(movementEntry);
    return this.http
      .post<RestMovementEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(movementEntry: IMovementEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(movementEntry);
    return this.http
      .put<RestMovementEntry>(`${this.resourceUrl}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(movementEntry: PartialUpdateMovementEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(movementEntry);
    return this.http
      .patch<RestMovementEntry>(`${this.resourceUrl}/${this.getMovementEntryIdentifier(movementEntry)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMovementEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMovementEntry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMovementEntryIdentifier(movementEntry: Pick<IMovementEntry, 'id'>): number {
    return movementEntry.id;
  }

  compareMovementEntry(o1: Pick<IMovementEntry, 'id'> | null, o2: Pick<IMovementEntry, 'id'> | null): boolean {
    return o1 && o2 ? this.getMovementEntryIdentifier(o1) === this.getMovementEntryIdentifier(o2) : o1 === o2;
  }

  addMovementEntryToCollectionIfMissing<Type extends Pick<IMovementEntry, 'id'>>(
    movementEntryCollection: Type[],
    ...movementEntriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const movementEntries: Type[] = movementEntriesToCheck.filter(isPresent);
    if (movementEntries.length > 0) {
      const movementEntryCollectionIdentifiers = movementEntryCollection.map(
        movementEntryItem => this.getMovementEntryIdentifier(movementEntryItem)!
      );
      const movementEntriesToAdd = movementEntries.filter(movementEntryItem => {
        const movementEntryIdentifier = this.getMovementEntryIdentifier(movementEntryItem);
        if (movementEntryCollectionIdentifiers.includes(movementEntryIdentifier)) {
          return false;
        }
        movementEntryCollectionIdentifiers.push(movementEntryIdentifier);
        return true;
      });
      return [...movementEntriesToAdd, ...movementEntryCollection];
    }
    return movementEntryCollection;
  }

  protected convertDateFromClient<T extends IMovementEntry | NewMovementEntry | PartialUpdateMovementEntry>(movementEntry: T): RestOf<T> {
    return {
      ...movementEntry,
      startDate: movementEntry.startDate?.format(DATE_FORMAT) ?? null,
      startTime: movementEntry.startTime?.toJSON() ?? null,
      endDate: movementEntry.endDate?.format(DATE_FORMAT) ?? null,
      endTime: movementEntry.endTime?.toJSON() ?? null,
      createdAt: movementEntry.createdAt?.format(DATE_FORMAT) ?? null,
      updatedAt: movementEntry.updatedAt?.format(DATE_FORMAT) ?? null,
      sanctionAt: movementEntry.sanctionAt?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMovementEntry: RestMovementEntry): IMovementEntry {
    return {
      ...restMovementEntry,
      startDate: restMovementEntry.startDate ? dayjs(restMovementEntry.startDate) : undefined,
      startTime: restMovementEntry.startTime ? dayjs(restMovementEntry.startTime) : undefined,
      endDate: restMovementEntry.endDate ? dayjs(restMovementEntry.endDate) : undefined,
      endTime: restMovementEntry.endTime ? dayjs(restMovementEntry.endTime) : undefined,
      createdAt: restMovementEntry.createdAt ? dayjs(restMovementEntry.createdAt) : undefined,
      updatedAt: restMovementEntry.updatedAt ? dayjs(restMovementEntry.updatedAt) : undefined,
      sanctionAt: restMovementEntry.sanctionAt ? dayjs(restMovementEntry.sanctionAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMovementEntry>): HttpResponse<IMovementEntry> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMovementEntry[]>): HttpResponse<IMovementEntry[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
