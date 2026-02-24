interface LicenceHeaderProps {
    period: string;
    topic? : string;
    code?: string;
    level: string;
    NC : string
    CANT : string;
    title? : string;
    
};


export const GradesHeader = ({ period , topic , code , level , NC , CANT  , title  }: LicenceHeaderProps) => {
  

  return (
    <div className="flex flex-col justify-between w-full">
        <div className="flex gap-4">
            <div className="flex items-center justify-center w-16 h-16 rounded-lg bg-[var(--accent-light)] text-[var(--accent)] text-2xl font-semibold shrink-0">{title}</div>
            <div className="flex gap-2 w-full justify-between">
                <div className="flex flex-col justify-between text-sm">
                    <div>
                        <p><span className="font-medium">Periode :</span> {period}</p>
                        <p className="text-[var(--accent)]"><span className="font-medium">Revendications :</span> 0 </p>
                        
                    </div>
                    <div>
                        {
                            code && (
                                <><p><span className="font-medium">Matiere :</span> {topic}</p>
                                <p><span className="font-medium">Code :</span> {code}</p>
                               
                                </>
                            )
                        }
                         <p><span className="font-medium">Niveau :</span> {level}</p>
                    </div>
                </div>
                <div className="text-sm text-right">
                    {
                        code && (<p><span className="font-medium">Nombre d'étudiants :</span> {NC}</p>)
                    }
                    <p><span className="font-medium">NC:</span> {NC}</p>
                    <p><span className="font-medium">CANT :</span> {CANT}</p>
                    <p className="text-[var(--accent)]"><span className="font-medium">Total revendictions :</span> 0</p>
                </div>
            </div>
        </div>
    </div>
  )
} 
