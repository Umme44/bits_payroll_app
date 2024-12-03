import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBand, NewBand } from '../band.model';

export type PartialUpdateBand = Partial<IBand> & Pick<IBand, 'id'>;

type RestOf<T extends IBand | NewBand> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestBand = RestOf<IBand>;

export type NewRestBand = RestOf<NewBand>;

export type PartialUpdateRestBand = RestOf<PartialUpdateBand>;

export type EntityResponseType = HttpResponse<IBand>;
export type EntityArrayResponseType = HttpResponse<IBand[]>;

@Injectable({ providedIn: 'root' })
export class BandService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/bands');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(band: NewBand): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(band);
    return this.http.post<RestBand>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(band: IBand): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(band);
    return this.http.put<RestBand>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(band: PartialUpdateBand): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(band);
    return this.http
      .patch<RestBand>(`${this.resourceUrl}/${this.getBandIdentifier(band)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBand>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBand[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }


  queryPage(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBand[]>(`${this.resourceUrl}/page`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBandIdentifier(band: Pick<IBand, 'id'>): number {
    return band.id;
  }

  compareBand(o1: Pick<IBand, 'id'> | null, o2: Pick<IBand, 'id'> | null): boolean {
    return o1 && o2 ? this.getBandIdentifier(o1) === this.getBandIdentifier(o2) : o1 === o2;
  }

  addBandToCollectionIfMissing<Type extends Pick<IBand, 'id'>>(
    bandCollection: Type[],
    ...bandsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bands: Type[] = bandsToCheck.filter(isPresent);
    if (bands.length > 0) {
      const bandCollectionIdentifiers = bandCollection.map(bandItem => this.getBandIdentifier(bandItem)!);
      const bandsToAdd = bands.filter(bandItem => {
        const bandIdentifier = this.getBandIdentifier(bandItem);
        if (bandCollectionIdentifiers.includes(bandIdentifier)) {
          return false;
        }
        bandCollectionIdentifiers.push(bandIdentifier);
        return true;
      });
      return [...bandsToAdd, ...bandCollection];
    }
    return bandCollection;
  }

  protected convertDateFromClient<T extends IBand | NewBand | PartialUpdateBand>(band: T): RestOf<T> {
    return {
      ...band,
      createdAt: band.createdAt?.format(DATE_FORMAT) ?? null,
      updatedAt: band.updatedAt?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restBand: RestBand): IBand {
    return {
      ...restBand,
      createdAt: restBand.createdAt ? dayjs(restBand.createdAt) : undefined,
      updatedAt: restBand.updatedAt ? dayjs(restBand.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBand>): HttpResponse<IBand> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBand[]>): HttpResponse<IBand[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
