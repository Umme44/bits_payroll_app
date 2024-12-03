import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkingExperience, NewWorkingExperience } from '../working-experience.model';

export type PartialUpdateWorkingExperience = Partial<IWorkingExperience> & Pick<IWorkingExperience, 'id'>;

type RestOf<T extends IWorkingExperience | NewWorkingExperience> = Omit<T, 'dojOfLastOrganization' | 'dorOfLastOrganization'> & {
  dojOfLastOrganization?: string | null;
  dorOfLastOrganization?: string | null;
};

export type RestWorkingExperience = RestOf<IWorkingExperience>;

export type NewRestWorkingExperience = RestOf<NewWorkingExperience>;

export type PartialUpdateRestWorkingExperience = RestOf<PartialUpdateWorkingExperience>;

export type EntityResponseType = HttpResponse<IWorkingExperience>;
export type EntityArrayResponseType = HttpResponse<IWorkingExperience[]>;

@Injectable({ providedIn: 'root' })
export class WorkingExperienceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/working-experiences');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(workingExperience: IWorkingExperience): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workingExperience);
    return this.http
      .post<RestWorkingExperience>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(workingExperience: IWorkingExperience): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workingExperience);
    return this.http
      .put<RestWorkingExperience>(this.resourceUrl, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(workingExperience: PartialUpdateWorkingExperience): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workingExperience);
    return this.http
      .patch<RestWorkingExperience>(`${this.resourceUrl}/${this.getWorkingExperienceIdentifier(workingExperience)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWorkingExperience>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWorkingExperience[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  queryByEmployeeId(employeeId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWorkingExperience[]>(`${this.resourceUrl}/get-by-employee/${employeeId}`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWorkingExperienceIdentifier(workingExperience: Pick<IWorkingExperience, 'id'>): number {
    return workingExperience.id;
  }

  compareWorkingExperience(o1: Pick<IWorkingExperience, 'id'> | null, o2: Pick<IWorkingExperience, 'id'> | null): boolean {
    return o1 && o2 ? this.getWorkingExperienceIdentifier(o1) === this.getWorkingExperienceIdentifier(o2) : o1 === o2;
  }

  addWorkingExperienceToCollectionIfMissing<Type extends Pick<IWorkingExperience, 'id'>>(
    workingExperienceCollection: Type[],
    ...workingExperiencesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const workingExperiences: Type[] = workingExperiencesToCheck.filter(isPresent);
    if (workingExperiences.length > 0) {
      const workingExperienceCollectionIdentifiers = workingExperienceCollection.map(
        workingExperienceItem => this.getWorkingExperienceIdentifier(workingExperienceItem)!
      );
      const workingExperiencesToAdd = workingExperiences.filter(workingExperienceItem => {
        const workingExperienceIdentifier = this.getWorkingExperienceIdentifier(workingExperienceItem);
        if (workingExperienceCollectionIdentifiers.includes(workingExperienceIdentifier)) {
          return false;
        }
        workingExperienceCollectionIdentifiers.push(workingExperienceIdentifier);
        return true;
      });
      return [...workingExperiencesToAdd, ...workingExperienceCollection];
    }
    return workingExperienceCollection;
  }

  protected convertDateFromClient<T extends IWorkingExperience | NewWorkingExperience | PartialUpdateWorkingExperience>(
    workingExperience: T
  ): RestOf<T> {
    return {
      ...workingExperience,
      dojOfLastOrganization: workingExperience.dojOfLastOrganization?.format(DATE_FORMAT) ?? null,
      dorOfLastOrganization: workingExperience.dorOfLastOrganization?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restWorkingExperience: RestWorkingExperience): IWorkingExperience {
    return {
      ...restWorkingExperience,
      dojOfLastOrganization: restWorkingExperience.dojOfLastOrganization ? dayjs(restWorkingExperience.dojOfLastOrganization) : undefined,
      dorOfLastOrganization: restWorkingExperience.dorOfLastOrganization ? dayjs(restWorkingExperience.dorOfLastOrganization) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWorkingExperience>): HttpResponse<IWorkingExperience> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWorkingExperience[]>): HttpResponse<IWorkingExperience[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
