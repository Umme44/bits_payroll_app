import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFloor, NewFloor } from '../floor.model';

export type PartialUpdateFloor = Partial<IFloor> & Pick<IFloor, 'id'>;

type RestOf<T extends IFloor | NewFloor> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestFloor = RestOf<IFloor>;

export type NewRestFloor = RestOf<NewFloor>;

export type PartialUpdateRestFloor = RestOf<PartialUpdateFloor>;

export type EntityResponseType = HttpResponse<IFloor>;
export type EntityArrayResponseType = HttpResponse<IFloor[]>;

@Injectable({ providedIn: 'root' })
export class FloorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/floors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(floor: NewFloor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(floor);
    return this.http.post<RestFloor>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(floor: IFloor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(floor);
    return this.http
      .put<RestFloor>(`${this.resourceUrl}/${this.getFloorIdentifier(floor)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(floor: PartialUpdateFloor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(floor);
    return this.http
      .patch<RestFloor>(`${this.resourceUrl}/${this.getFloorIdentifier(floor)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFloor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFloor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFloorIdentifier(floor: Pick<IFloor, 'id'>): number {
    return floor.id;
  }

  compareFloor(o1: Pick<IFloor, 'id'> | null, o2: Pick<IFloor, 'id'> | null): boolean {
    return o1 && o2 ? this.getFloorIdentifier(o1) === this.getFloorIdentifier(o2) : o1 === o2;
  }

  addFloorToCollectionIfMissing<Type extends Pick<IFloor, 'id'>>(
    floorCollection: Type[],
    ...floorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const floors: Type[] = floorsToCheck.filter(isPresent);
    if (floors.length > 0) {
      const floorCollectionIdentifiers = floorCollection.map(floorItem => this.getFloorIdentifier(floorItem)!);
      const floorsToAdd = floors.filter(floorItem => {
        const floorIdentifier = this.getFloorIdentifier(floorItem);
        if (floorCollectionIdentifiers.includes(floorIdentifier)) {
          return false;
        }
        floorCollectionIdentifiers.push(floorIdentifier);
        return true;
      });
      return [...floorsToAdd, ...floorCollection];
    }
    return floorCollection;
  }

  protected convertDateFromClient<T extends IFloor | NewFloor | PartialUpdateFloor>(floor: T): RestOf<T> {
    return {
      ...floor,
      createdAt: floor.createdAt?.toJSON() ?? null,
      updatedAt: floor.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFloor: RestFloor): IFloor {
    return {
      ...restFloor,
      createdAt: restFloor.createdAt ? dayjs(restFloor.createdAt) : undefined,
      updatedAt: restFloor.updatedAt ? dayjs(restFloor.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFloor>): HttpResponse<IFloor> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFloor[]>): HttpResponse<IFloor[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
